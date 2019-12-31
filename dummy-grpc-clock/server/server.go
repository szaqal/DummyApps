package main

import (
	"context"
	"log"
	"net"
	"os"
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

	timeStampVal := &timestamp.Timestamp{Seconds: time.Now().Unix(), Nanos: int32(time.Now().Nanosecond())}

	resp := &pb.TimeResponse{Timestamp: timeStampVal}
	log.Println("Request Served")
	return resp, nil
}

func main() {
	lis, err := net.Listen("tcp", port)
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}
	// Creates a new gRPC server

	var srv *grpc.Server = nil
	if os.Getenv("USE_TLS") == "true" {
		creds, _ := credentials.NewServerTLSFromFile("service.pem", "service.key")
		srv = grpc.NewServer(grpc.Creds(creds))
		log.Println("GRPC with TLS")
	} else {
		log.Println("GRPC without TLS")
		srv = grpc.NewServer()
	}

	pb.RegisterSomeServiceServer(srv, &server{})

	srv.Serve(lis)
}
