FROM alpine:3.9.3

RUN apk add iftop
ADD dummy-grpc-testing-server /dummy-grpc-testing-server
RUN chmod +x /dummy-grpc-testing-server
CMD /dummy-grpc-testing-server