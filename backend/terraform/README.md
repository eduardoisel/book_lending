# Requirements
Check [this link](https://developer.hashicorp.com/terraform/tutorials/aws-get-started/aws-create) prerequisite. Also do
not forget the environment variables AWS_ACCESS_KEY_ID AWS_SECRET_ACCESS_KEY mentioned later.

For automation of creation of database schema and adding postgis to the base postgres image aws provides, psql is used
on the machine running terraform. If one does not want to install it, check for alternatives.

## Before terraform

Do not forget to create the java jar to be placed on the virtual machine. It is expected to exist.

# Instructions

* Run terraform init on this folder
* Assign values to the variables declared in variables.tf. [This site](https://developer.hashicorp.com/terraform/language/values/variables#assign-values-to-variables) explains how to do it.
* Use the command terraform apply to create the resources
* Run the java string from the output through ssh (can be done by uncommenting remote-exec in terraform, but will cause it to never end, as server programs will not end)

## SSH

The code will create an ssh key to connect with the virtual machine, installing java tools and transferring the jar.
It automatically registers the public key on amazon, and stores the private key on the ssh-key-pair.pem folder. This is 
the equivalent of using the [aws ec2 create-key-pair command](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/create-key-pairs.html).

If one wants to reuse a key pair already created, see the [import-key-pair](https://docs.aws.amazon.com/cli/latest/reference/ec2/import-key-pair.html),
and using the browser to import it should also be possible. Afterwards remove the automatic key creation from terraform
and use the commented key variables

# TODO

* Develop heavily the [virtual private cloud](https://docs.aws.amazon.com/vpc/latest/userguide/what-is-amazon-vpc.html) to not use the default.
* Separate into modules
* See how to create multiple server instances as to support increase on usage by running multiple instances
