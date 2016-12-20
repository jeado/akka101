#!/usr/bin/env bash
ip=`docker-machine ip \`docker-machine active\``
echo 'SET KAFKA_ADVERTISED_HOST_NAME =' $ip
#sbt docker
export KAFKA_ADVERTISED_HOST_NAME=$ip
docker-compose rm -f
docker-compose build
docker-compose up