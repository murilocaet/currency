resource "aws_security_group" "murilocosta-group" {
  name        = "murilocosta-ssh"
  description = "murilocosta-ssh"

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = "${var.cdirs_acesso_ssh}"
  }
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = "${var.cdirs_acesso_public}"
  }
  ingress {
    from_port   = 8081
    to_port     = 8081
    protocol    = "tcp"
    cidr_blocks = "${var.cdirs_acesso_public}"
  }
  ingress {
    from_port   = 8100
    to_port     = 8100
    protocol    = "tcp"
    cidr_blocks = "${var.cdirs_acesso_public}"
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = "${var.cdirs_acesso_public}"
  }
  tags = {
    Name = "global"
  }
}