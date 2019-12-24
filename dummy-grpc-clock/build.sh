#!/bin/bash

client=$(pwd)/client
server=$(pwd)/server

echo "Protogen"
protoc -I service/ service/service.proto --go_out=plugins=grpc:service

echo "Build Module"
go build ./...

echo "Build Server"
cd $server && go build 
CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -o dummy-grpc-clock-server
docker build -t dummy-grpc-clock-server .
docker tag dummy-grpc-clock-server szaqal/dummy-grpc-clock-server:latest
docker push szaqal/dummy-grpc-clock-server:latest

echo "Build Client"
cd $client && go build
CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -o dummy-grpc-clock-client
docker build -t dummy-grpc-clock-client .
docker tag dummy-grpc-clock-client szaqal/dummy-grpc-clock-client:latest
docker push szaqal/dummy-grpc-clock-client:latest
