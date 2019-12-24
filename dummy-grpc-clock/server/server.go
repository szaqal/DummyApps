package main

import (
	"context"
	"log"
	"net"
	"time"

	empty "github.com/golang/protobuf/ptypes/empty"
	timestamp "github.com/golang/protobuf/ptypes/timestamp"
	pb "github.com/szaqal/DummyApps/dummy-grpc-clock/service"

	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials"
)

const (
	port = ":50051"
)

type server struct {
}

func (s *server) Perform(ctx context.Context, params *empty.Empty) (*pb.TimeResponse, error) {

	resp := &pb.TimeResponse{Timestamp: &timestamp.Timestamp{Seconds: time.Now().UnixNano()}}
	return resp, nil
}

func main() {
	creds, err := credentials.NewServerTLSFromFile("service.pem", "service.key")
	lis, err := net.Listen("tcp", port)
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}
	// Creates a new gRPC server
	s := grpc.NewServer(grpc.Creds(creds))

	pb.RegisterSomeServiceServer(s, &server{})

	s.Serve(lis)
}
