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