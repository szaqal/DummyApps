package main

import (
	"context"
	"io"
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
	resp := &pb.ServiceResponse{Message: generateMessage(1)}
	log.Println("Request Served in:", time.Since(timeStamp))
	return resp, nil
}

func (s *server) PerformClientStream(stream pb.DelayService_PerformClientStreamServer) error {
	timeStamp := time.Now()
	i := 0
	for {
		_, err := stream.Recv()
		if err == io.EOF {
			log.Printf("Request Served in: %s | %d client requests", time.Since(timeStamp), i)
			return stream.SendAndClose(&pb.ServiceResponse{Message: generateMessage(i)})
		}
		i++
		if err != nil {
			return err
		}
	}
}

func (s *server) PerformBiStream(stream pb.DelayService_PerformBiStreamServer) error {
	for {
		timeStamp := time.Now()
		_, err := stream.Recv()

		if err == io.EOF {
			return nil
		}
		if err != nil {
			return err
		}
		log.Printf("Request Served in: %s | %d client requests", time.Since(timeStamp), 1)
		if err := stream.Send(&pb.ServiceResponse{Message: generateMessage(1)}); err != nil {
			return err
		}
	}
}

func generateMessage(multiplier int) []byte {
	//1048576 1MB
	token := make([]byte, multiplier*1048576)
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
