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

	var dialOption grpc.DialOption = nil
	if os.Getenv("USE_TLS") == "true" {
		log.Println("GRPC with TLS")
		dialOption = grpc.WithTransportCredentials(credentials.NewTLS(config))
	} else {
		log.Println("GRPC without TLS")
		dialOption = grpc.WithInsecure()
	}

	conn, err := grpc.Dial(address, dialOption)
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
