
output "currency-ip" {
  value = "${aws_instance.currency-vm.public_ip}"
}
output "currency-dns" {
  value = "${aws_instance.currency-vm.public_dns}"
}