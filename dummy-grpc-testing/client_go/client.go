package main

import (
	"context"
	"log"
	"os"
	"time"

	"github.com/golang/protobuf/ptypes/empty"
	pb "github.com/szaqal/DummyApps/dummy-grpc-testing/service"
	"google.golang.org/grpc"
)

var (
	address = "localhost:50051"
)

func main() {

	serverAddress := os.Getenv("SERVER_ADDRESS")
	if serverAddress != "" {
		address = serverAddress
	}

	var dialOption grpc.DialOption = nil
	dialOption = grpc.WithInsecure()

	conn, err := grpc.Dial(address, dialOption)
	if err != nil {
		log.Fatalf("did not connect: %v", err)
	}
	defer conn.Close()

	client := pb.NewDelayServiceClient(conn)

	for {
		resp, err := client.Perform(context.Background(), &empty.Empty{})
		if err != nil {
			log.Fatalf(err.Error())
		}
		log.Println(resp.GetMessage())
		<-time.After(1 * time.Second)
	}
}
