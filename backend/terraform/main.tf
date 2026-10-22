provider "aws" {
  region = var.region
}


data "aws_ami" "ubuntu" {
  most_recent = true

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd-gp3/ubuntu-noble-24.04-amd64-server-*"]
  }

  owners = ["099720109477"] # Canonical
}

data "local_file" "server_jar" {
  filename = "${path.root}/../build/libs/backend-1.0-SNAPSHOT.jar"

  lifecycle {
    postcondition {
      condition     =  fileexists(self.filename)
      error_message = "The required file does not exist at: ${self.filename}"
    }
  }

}


#next resources below can be removed if using key created in other means, see variables
#creates key
resource "tls_private_key" "ssh-keypair" {
  algorithm = "RSA"
  rsa_bits  = 2048
}

#stores private on file
resource "local_file" "private-key" {
  content  = tls_private_key.ssh-keypair.private_key_pem
  filename = "${path.root}/ssh-key-pair.pem"
}

#gives the public key to be stored linked to the name given
resource "aws_key_pair" "ssh-keypair" {
  key_name   = "ssh-key-pair" #should be unique on the scope of the account (or maybe region and account?)
  public_key = tls_private_key.ssh-keypair.public_key_openssh
}

resource "aws_security_group" "ssh" {
  name = "terraform-vm-ssh"

  /*
  ingress {
    description = "SSH from my IP"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = [var.my_ip_cidr] # limits SSH only from your IP address
  }

  
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
  */
}

resource "aws_vpc_security_group_ingress_rule" "allow_ssh_to_self" {
  security_group_id = aws_security_group.ssh.id

  description = "SSH from my IP"
  from_port   = 22
  to_port     = 22
  ip_protocol = "tcp" #ssh uses tcp. Should also be possible to limit for ssh protocol, todo see
  cidr_ipv4   = var.my_ip_cidr # limits SSH only from your IP address

}

resource "aws_vpc_security_group_egress_rule" "allow_ssh_to_self" {
  security_group_id = aws_security_group.ssh.id

  description = "SSH to my IP"
  from_port   = 22
  to_port     = 22
  ip_protocol = "tcp" #ssh uses tcp. Should also be possible to limit for ssh protocol, todo see
  cidr_ipv4   = var.my_ip_cidr # limits SSH only to your IP address
}

resource "aws_security_group" "server_port" {
  name = "terraform-vm-server-ports"

  ingress {
    description = "Acceptable ports to use tcp"
    from_port   = 8000
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # necessary to permit all (could probably have some restrictions), see NOTE next to allow_all_traffic on aws_security_rule documentation
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "app_server" {
  ami           = data.aws_ami.ubuntu.id
  instance_type = var.instance_type

  /*
  network_interface { # primary_network_interface on later aws providers
    network_interface_id = aws_network_interface.example.id
    delete_on_termination = true
    device_index = 0
  }
  */

  key_name               = aws_key_pair.ssh-keypair.key_name # var.key_pair_name
  vpc_security_group_ids = [aws_security_group.ssh.id, aws_security_group.server_port.id]

  user_data = <<EOF
          #!/bin/bash
          sudo apt update -y
          sudo apt-get -y install openjdk-25-jdk
          EOF

  # Connect using the instance's public IP
  # aws ec2 describe-instances                COMMAND
  connection {
    type        = "ssh"
    user        = "ubuntu" # the same as the operating system by default?
    host        = self.public_ip
    private_key = file(local_file.private-key.filename) # file(var.private_key_path)
    timeout     = "5m"                                  # should be enough
  }


  provisioner "file" {
    source      = data.local_file.server_jar.filename
    destination = "/home/ubuntu/bookServer.jar"
  }

  # How to override default values found in application properties
  # https://docs.spring.io/spring-boot/how-to/webserver.html#howto.webserver.change-port
  # block below will run spring and not stop, never letting terraform end.
  /*
  provisioner "remote-exec" {
    inline = [
      "java -jar /home/ubuntu/bookServer.jar --spring.datasource.password=${var.database_password} --spring.datasource.username=${var.database_username} --spring.datasource.url=jdbc:postgresql://${aws_db_instance.postgres_database.address}:${var.database_port}/${var.database_name}"
    ]
  }
  */


  tags = {
    Name = var.instance_name
  }
}




