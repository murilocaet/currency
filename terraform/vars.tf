variable amis {
  type = map

  default = {
      "us-east-1" = "ami-0279c3b3186e54acd"
  }
}

variable instance_type {
  type = map

  default = {
      "instance_type_currency" = "t2.micro"
  }
}

variable cdirs_acesso_ssh {
  type = list
  default = ["186.228.224.33/32"]
}

variable cdirs_acesso_public {
  type = list
  default = ["0.0.0.0/0"]
}

variable key_name {
  default = "terraform-aws"
}