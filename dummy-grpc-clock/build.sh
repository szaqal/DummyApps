#!/bin/bash

client=$(pwd)/client
server=$(pwd)/server

echo "Protogen"
protoc -I service/ service/service.proto --go_out=plugins=grpc:service

echo "Build Module"
go build ./...
echo "Build Server"
cd $server && go build 
echo "Build Client"
cd $client && go build
