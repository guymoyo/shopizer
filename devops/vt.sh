#!/bin/bash
#service vetement

cd ~/projets/vt/sm-shop/

nohup mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8082 -Dspring-boot.run.profiles=prod-vt > trace.log &