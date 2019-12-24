#!/bin/bash

echo "Protogen"
protoc -I service/ service/service.proto --go_out=plugins=grpc:service

echo "Build"
go build ./...
