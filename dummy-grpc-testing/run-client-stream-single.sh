#!/bin/bash

source run-client-setup.sh

export CALLER_TYPE=streamsingle
time java -Xmx8g -jar client_java/grpc-client/target/grpc-client-1.0-SNAPSHOT-jar-with-dependencies.jar
