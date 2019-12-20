#!/bin/bash
#set -x
CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -o dummy-http
docker build -t dummy-http .
docker tag dummy-http szaqal/dummy-http:latest
docker push szaqal/dummy-http:latest
