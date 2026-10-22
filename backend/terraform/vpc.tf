/*
resource "aws_vpc" "permissive" {
  cidr_block = "85.246.0.0/16"

  tags = {
    name = "Book_vpc"
  }
}

resource "aws_subnet" "encompassing" {
  vpc_id     = aws_vpc.permissive.id
  cidr_block = "85.246.0.0/16"

  tags = {
    Name = "Book_subnet"
  }

}

resource "aws_network_interface" "example" {
  subnet_id = aws_subnet.encompassing.id
  #private_ips = ["172.16.10.100"]

  tags = {
    Name = "Book_network_interface"
  }
}
*/