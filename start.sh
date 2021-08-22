#!/bin/bash

docker build --tag java-docker-drillapp .
docker run --name drillapp --publish 8080:8080 java-docker-drillapp