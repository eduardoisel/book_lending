variable "instance_name" {
  description = "Value of the EC2 instance's Name tag."
  type        = string
  default     = "book_lending_server"
}

variable "instance_type" {
  description = "The EC2 instance's type."
  type        = string
  #aws ec2 describe-instance-types --filters Name=free-tier-eligible,Values=true
  default = "t3.micro" #default due to being free
}

#https://docs.aws.amazon.com/global-infrastructure/latest/regions/aws-regions.html
variable "region" {
  description = "Region for the infraestructure"
  type        = string
  default     = "eu-west-1" #close to me
}

variable "database_port" {
  description = "Port used for database"
  type        = string
  default     = "5433"
}

variable "database_name" {
  description = "Name given to database"
  type        = string
  default     = "books"
}

#private values

variable "database_username" {
  description = "Username necessary to access database"
  type        = string
  sensitive   = true
}

variable "database_password" {
  description = "Password to access database"
  type        = string
  sensitive   = true
}

# can use variables below if to reuse the same key, could be useful if one wants to manually connect with ssh manually.
# https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/create-key-pairs.html if none was yet created
# or else aws ec2 import-key-pair --key-name [yout key name] --public-key-material fileb://[key file path] to register yours on aws
# Use aws cli to get private key with your --key-name
/*
variable "key_pair_name" {
  description = "Name of an existing EC2 key pair"
  type        = string
}

variable "private_key_path" {
  description = "Path to the corresponding local private key"
  type        = string
}
*/

variable "my_ip_cidr" {
  description = "Your public IP in CIDR notation, e.g. 203.0.113.10/32"
  type        = string
  sensitive   = true
}
