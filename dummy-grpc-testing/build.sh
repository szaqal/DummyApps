#!/bin/bash

echo "Proto (GO)"
protoc -I service/ service/service.proto --go_out=plugins=grpc:service


client=$(pwd)/client_go
client_java=$(pwd)/client_java
server=$(pwd)/server

echo "Build Server"
cd $server && go build
CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -o dummy-grpc-testing-server
docker build -t dummy-grpc-testing-server .
docker tag dummy-grpc-testing-server szaqal/dummy-grpc-testing-server:latest
docker push szaqal/dummy-grpc-testing-server:latest

echo "Build Client (GO)"
cd $client && go build
CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -o dummy-grpc-testing-client
docker build -t dummy-grpc-testing-client .
docker tag dummy-grpc-testing-client szaqal/dummy-grpc-testing-client:latest
docker push szaqal/dummy-grpc-testing-client:latest


echo "Build Client (JAVA)"
cd $client_java && mvn -f grpc-client/pom.xml clean install 
