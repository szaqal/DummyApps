#!/bin/bash
#set -x
CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -o dummy-http-client
docker build -t dummy-http-client .
docker tag dummy-http-client szaqal/dummy-http-client:latest
docker push szaqal/dummy-http-client:latest
