package main

import (
	"context"
	"log"
	"net"
	"time"

	"github.com/golang/protobuf/ptypes/empty"
	"google.golang.org/grpc"

	pb "github.com/szaqal/DummyApps/dummy-grpc-testing/service"
)

const (
	port = ":50051"
)

type server struct {
}

func (s *server) Perform(ctx context.Context, params *empty.Empty) (*pb.ServiceResponse, error) {

	resp := &pb.ServiceResponse{Message: "Hi"}
	log.Println("Request Served")
	<-time.After(500 * time.Millisecond)
	return resp, nil
}

func main() {
	lis, err := net.Listen("tcp", port)
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}
	var srv *grpc.Server = grpc.NewServer()
	pb.RegisterDelayServiceServer(srv, &server{})
	srv.Serve(lis)
}
