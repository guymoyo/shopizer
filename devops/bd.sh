#!/bin/bash
#service Bande dessine

cd ~/projets/bd/sm-shop/

nohup mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=9092 -Dspring-boot.run.profiles=prod-bd > trace.log &