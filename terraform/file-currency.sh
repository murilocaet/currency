#!/bin/bash

sudo apt install apt-transport-https ca-certificates curl software-properties-common -y
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu bionic stable"
sudo apt-get update
apt-cache search docker-ce
sudo apt install docker-ce -y
sudo chmod 666 /var/run/docker.sock
sudo apt install docker-compose -y

sudo mkdir /var/www/
sudo chmod o+w /var/www/
sudo mkdir /var/www/currency
sudo chmod o+w /var/www/currency
sudo mkdir /var/www/currency/files
sudo chmod o+w /var/www/currency/files
sudo mkdir /var/www/currency/files/front
sudo chmod o+w /var/www/currency/files/front
sudo mkdir /var/www/currency/files/swagger
sudo chmod o+w /var/www/currency/files/swagger

cd /var/www/currency

sudo bash -c "echo \"version: 3.3
services: 

  swaggerui:
    image: swaggerapi/swagger-ui
    container_name: swaggerui
    restart: always
    ports:
      - 8100:8080
    volumes:
      - ./files/swagger/openapi.json:/spec/openapi.json
    environment:
      BASE_URL: /swagger
      SWAGGER_JSON: /spec/openapi.json
    depends_on:
      - 'backend'

  redis:
    image: redis:latest
    container_name: redis
    environment:
       REDIS_PASSWORD: Redis12345!
    ports:
      - 6379:6379
    networks:
      - backserver
  
  backend:
    image: mccosta/currency-java
    container_name: backend
    ports:
      - 8081:8081
    networks:
      - backserver
      - frontend
    depends_on:
      - 'redis'
        
  currency:
    image: mccosta/currency:1.0
    container_name: currency
    ports:
      - 80:80
    volumes:
      - ./files/front/default.conf:/etc/nginx/conf.d/default.conf
    networks: 
      - frontend
    depends_on:
      - 'backend'

networks:
  frontend:
    driver: bridge
  backserver:
    driver: bridge\" > docker-compose.yml" 


sudo sed -i 's/3.3/\"3.3\"/' docker-compose.yml

cd /var/www/currency/files/front

sudo bash -c "echo \"server {
    listen       80;
    server_name  localhost;

    location / {
        root   /var/www/currency;
        index  index.html index.htm;
    }
}\" > default.conf" 

cd /var/www/currency/

docker-compose up -d