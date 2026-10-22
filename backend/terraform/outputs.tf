
output "database_address" {
  description = "The address (or host) of the database"
  value       = aws_db_instance.postgres_database.address
}


output "instance_hostname" {
  description = "Private DNS name of the EC2 instance."
  value       = aws_instance.app_server.private_dns
}

output "vm_public_ip" {
  description = "Public IP address of the VM"
  value       = aws_instance.app_server.public_ip
}

output "ssh_command" {
  description = "SSH command to connect to the VM"
  value       = "ssh -i ${local_file.private-key.filename} ubuntu@${aws_instance.app_server.public_ip}"
}



