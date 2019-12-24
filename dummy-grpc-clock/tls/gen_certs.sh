#!/bin/bash

echo "CA" && \
openssl genrsa -out ca.key 4096 && \
openssl req -new -x509 -key ca.key -sha256 -subj "/C=PL/ST=MA/O=XXX, Inc." -days 365 -out ca.cert && \
echo "Service Key" && \
openssl genrsa -out service.key 4096
echo "CSR" && \
openssl req -new -key service.key -out service.csr -config certificate.conf && \
echo "Service CERT" && \
openssl x509 -req -in service.csr -CA ca.cert -CAkey ca.key -CAcreateserial -out service.pem -days 365 -sha256 -extfile certificate.conf -extensions req_ext 
