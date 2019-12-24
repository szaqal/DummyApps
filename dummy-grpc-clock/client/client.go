package main

import (
	"context"
	"crypto/tls"
	"log"
	"os"
	"time"

	empty "github.com/golang/protobuf/ptypes/empty"
	pb "github.com/szaqal/DummyApps/dummy-grpc-clock/service"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials"
)

var (
	address = "localhost:50051"
)

func main() {

	serverAddress := os.Getenv("SERVER_ADDRESS")
	if serverAddress != "" {
		address = serverAddress
	}

	config := &tls.Config{
		InsecureSkipVerify: true,
	}

	conn, err := grpc.Dial(address, grpc.WithTransportCredentials(credentials.NewTLS(config)))
	if err != nil {
		log.Fatalf("did not connect: %v", err)
	}
	defer conn.Close()

	client := pb.NewSomeServiceClient(conn)

	for {
		resp, err := client.Perform(context.Background(), &empty.Empty{})
		if err != nil {
			log.Fatalf(err.Error())
		}
		timeStamp := time.Unix(resp.Timestamp.GetSeconds(), int64(resp.Timestamp.GetNanos()))
		log.Println(timeStamp)
		<-time.After(1 * time.Second)
	}
}
