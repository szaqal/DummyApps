package main

import (
	"context"
	"log"
	"math/rand"
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

	timeStamp := time.Now()
	resp := &pb.ServiceResponse{Message: generateMessage()}
	log.Println("Request Served in:", time.Since(timeStamp))
	return resp, nil
}

func generateMessage() []byte {
	token := make([]byte, 2097152) //2MB
	rand.Read(token)
	return token
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
