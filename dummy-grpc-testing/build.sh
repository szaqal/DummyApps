#!/bin/bash

echo "Protogen"
protoc -I service/ service/service.proto --go_out=plugins=grpc:service


client=$(pwd)/client_go
server=$(pwd)/server

echo "Build Server"
cd $server && go build
CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -o dummy-grpc-clock-server

echo "Build Client"
cd $client && go build
CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -o dummy-grpc-clock-client