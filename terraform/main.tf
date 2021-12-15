terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 3.27"
    }
  }

  required_version = ">= 0.14.9"
}

provider "aws" {
  profile = "default"
  region  = "us-east-1"
}

resource "aws_instance" "currency-vm" {
  ami = "${var.amis["us-east-1"]}"
  instance_type = "${var.instance_type["instance_type_currency"]}"
  key_name = "${var.key_name}"
  user_data = file("file-currency.sh")
  tags = {
    Name = "currency-vm"
  }
  vpc_security_group_ids = ["${aws_security_group.murilocosta-group.id}"]
}