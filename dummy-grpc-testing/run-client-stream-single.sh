#!/bin/bash

export CALLER_TYPE=streamsingle
export ITERATIONS_COUNT=50
export THREAD_POOL_SIZE=20
export WORKER_POOL_SIZE=100
export THREAD_POOL_QUEUE=100
time java -Xmx8g -jar client_java/grpc-client/target/grpc-client-1.0-SNAPSHOT-jar-with-dependencies.jar
