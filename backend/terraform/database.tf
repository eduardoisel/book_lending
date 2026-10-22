data "local_file" "creation_schema" {
  filename = "${path.root}/../sql/creation.sql"

  lifecycle {
    # needs to be post to have self
    postcondition {
      condition     =  fileexists(self.filename)
      error_message = "The required file does not exist at: ${self.filename}"
    }
  }

}


# Attempt at using dbeaver. Anyone who prefers dbeaver I recommend checking the link and see how to fix it
# https://github.com/dbeaver/dbeaver/wiki/Command-Line indicates dbeaver.exe (for  windows), tried command below, opens gui but does not connect
# dbeaverc.exe -con "driver=postgres|name=[con_name]|port=5433|user=var.u|password=var.p|url=[db_instance.address]"
resource "aws_db_instance" "postgres_database" {
  allocated_storage   = 8
  db_name             = var.database_name
  engine              = "postgres"
  instance_class      = "db.t3.micro"
  username            = var.database_username
  password            = var.database_password
  port                = var.database_port
  publicly_accessible = true # should be necessary to access from personal machine. Necessary to create schema

  # deletion does not require snapshot beforehand. Set to true as its in development phase, and should not hold any special values
  skip_final_snapshot = true


  provisioner "local-exec" {
    interpreter = ["cmd", "/C"] # uses windows command line, change for other operating systems
    # https://truststore.pki.rds.amazonaws.com/global/global-bundle.pem link for aws recommended file
    command = "psql --host=${self.address} --port=${self.port} --dbname=${self.db_name} --username=${self.username} -f ${data.local_file.creation_schema.filename}"

    environment = {
      PGPASSWORD = var.database_password # Alternative to passing via command, otherwise command requests input from user
    }
  }

}