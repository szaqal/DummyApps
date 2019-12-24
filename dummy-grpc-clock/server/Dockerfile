FROM alpine:3.9.3

ADD dummy-grpc-clock-server /dummy-grpc-clock-server
ADD service.pem /service.pem
ADD service.key /service.key
RUN chmod +x /dummy-grpc-clock-server
CMD /dummy-grpc-clock-server 
