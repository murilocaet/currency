FROM nginx:latest
MAINTAINER Murilo Costa
WORKDIR /var/www/currency
COPY ./build/ /var/www/currency
EXPOSE 80