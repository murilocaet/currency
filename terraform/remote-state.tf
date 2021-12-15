terraform {
  backend "remote" {
    hostname = "app.terraform.io"
    organization = "murilocosta"

    workspaces {
      name = "aws-murilocosta"
    }
  }
}