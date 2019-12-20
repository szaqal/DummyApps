#!/bin/bash
#set -x
go build -o dummy-http
docker build -t dummy-http .
docker tag dummy-http szaqal/dummy-http:latest
docker push szaqal/dummy-http:latest