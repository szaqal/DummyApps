// Code generated by protoc-gen-go. DO NOT EDIT.
// source: service.proto

package service

import (
	context "context"
	fmt "fmt"
	proto "github.com/golang/protobuf/proto"
	empty "github.com/golang/protobuf/ptypes/empty"
	grpc "google.golang.org/grpc"
	codes "google.golang.org/grpc/codes"
	status "google.golang.org/grpc/status"
	math "math"
)

// Reference imports to suppress errors if they are not otherwise used.
var _ = proto.Marshal
var _ = fmt.Errorf
var _ = math.Inf

// This is a compile-time assertion to ensure that this generated file
// is compatible with the proto package it is being compiled against.
// A compilation error at this line likely means your copy of the
// proto package needs to be updated.
const _ = proto.ProtoPackageIsVersion3 // please upgrade the proto package

type ServiceResponse struct {
	Message              string   `protobuf:"bytes,1,opt,name=message,proto3" json:"message,omitempty"`
	XXX_NoUnkeyedLiteral struct{} `json:"-"`
	XXX_unrecognized     []byte   `json:"-"`
	XXX_sizecache        int32    `json:"-"`
}

func (m *ServiceResponse) Reset()         { *m = ServiceResponse{} }
func (m *ServiceResponse) String() string { return proto.CompactTextString(m) }
func (*ServiceResponse) ProtoMessage()    {}
func (*ServiceResponse) Descriptor() ([]byte, []int) {
	return fileDescriptor_a0b84a42fa06f626, []int{0}
}

func (m *ServiceResponse) XXX_Unmarshal(b []byte) error {
	return xxx_messageInfo_ServiceResponse.Unmarshal(m, b)
}
func (m *ServiceResponse) XXX_Marshal(b []byte, deterministic bool) ([]byte, error) {
	return xxx_messageInfo_ServiceResponse.Marshal(b, m, deterministic)
}
func (m *ServiceResponse) XXX_Merge(src proto.Message) {
	xxx_messageInfo_ServiceResponse.Merge(m, src)
}
func (m *ServiceResponse) XXX_Size() int {
	return xxx_messageInfo_ServiceResponse.Size(m)
}
func (m *ServiceResponse) XXX_DiscardUnknown() {
	xxx_messageInfo_ServiceResponse.DiscardUnknown(m)
}

var xxx_messageInfo_ServiceResponse proto.InternalMessageInfo

func (m *ServiceResponse) GetMessage() string {
	if m != nil {
		return m.Message
	}
	return ""
}

func init() {
	proto.RegisterType((*ServiceResponse)(nil), "service.ServiceResponse")
}

func init() { proto.RegisterFile("service.proto", fileDescriptor_a0b84a42fa06f626) }

var fileDescriptor_a0b84a42fa06f626 = []byte{
	// 148 bytes of a gzipped FileDescriptorProto
	0x1f, 0x8b, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0xff, 0xe2, 0xe2, 0x2d, 0x4e, 0x2d, 0x2a,
	0xcb, 0x4c, 0x4e, 0xd5, 0x2b, 0x28, 0xca, 0x2f, 0xc9, 0x17, 0x62, 0x87, 0x72, 0xa5, 0xa4, 0xd3,
	0xf3, 0xf3, 0xd3, 0x73, 0x52, 0xf5, 0xc1, 0xc2, 0x49, 0xa5, 0x69, 0xfa, 0xa9, 0xb9, 0x05, 0x25,
	0x95, 0x10, 0x55, 0x4a, 0xda, 0x5c, 0xfc, 0xc1, 0x10, 0x75, 0x41, 0xa9, 0xc5, 0x05, 0xf9, 0x79,
	0xc5, 0xa9, 0x42, 0x12, 0x5c, 0xec, 0xb9, 0xa9, 0xc5, 0xc5, 0x89, 0xe9, 0xa9, 0x12, 0x8c, 0x0a,
	0x8c, 0x1a, 0x9c, 0x41, 0x30, 0xae, 0x91, 0x2f, 0x17, 0x8f, 0x4b, 0x6a, 0x4e, 0x62, 0x25, 0x54,
	0x87, 0x90, 0x2d, 0x17, 0x7b, 0x41, 0x6a, 0x51, 0x5a, 0x7e, 0x51, 0xae, 0x90, 0x98, 0x1e, 0xc4,
	0x16, 0x3d, 0x98, 0x2d, 0x7a, 0xae, 0x20, 0x5b, 0xa4, 0x24, 0xf4, 0x60, 0xae, 0x42, 0xb3, 0x46,
	0x89, 0x21, 0x89, 0x0d, 0xac, 0xd6, 0x18, 0x10, 0x00, 0x00, 0xff, 0xff, 0xf8, 0xc4, 0x6a, 0x7f,
	0xb9, 0x00, 0x00, 0x00,
}

// Reference imports to suppress errors if they are not otherwise used.
var _ context.Context
var _ grpc.ClientConn

// This is a compile-time assertion to ensure that this generated file
// is compatible with the grpc package it is being compiled against.
const _ = grpc.SupportPackageIsVersion4

// DelayServiceClient is the client API for DelayService service.
//
// For semantics around ctx use and closing/ending streaming RPCs, please refer to https://godoc.org/google.golang.org/grpc#ClientConn.NewStream.
type DelayServiceClient interface {
	Perform(ctx context.Context, in *empty.Empty, opts ...grpc.CallOption) (*ServiceResponse, error)
}

type delayServiceClient struct {
	cc *grpc.ClientConn
}

func NewDelayServiceClient(cc *grpc.ClientConn) DelayServiceClient {
	return &delayServiceClient{cc}
}

func (c *delayServiceClient) Perform(ctx context.Context, in *empty.Empty, opts ...grpc.CallOption) (*ServiceResponse, error) {
	out := new(ServiceResponse)
	err := c.cc.Invoke(ctx, "/service.DelayService/perform", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

// DelayServiceServer is the server API for DelayService service.
type DelayServiceServer interface {
	Perform(context.Context, *empty.Empty) (*ServiceResponse, error)
}

// UnimplementedDelayServiceServer can be embedded to have forward compatible implementations.
type UnimplementedDelayServiceServer struct {
}

func (*UnimplementedDelayServiceServer) Perform(ctx context.Context, req *empty.Empty) (*ServiceResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method Perform not implemented")
}

func RegisterDelayServiceServer(s *grpc.Server, srv DelayServiceServer) {
	s.RegisterService(&_DelayService_serviceDesc, srv)
}

func _DelayService_Perform_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(empty.Empty)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(DelayServiceServer).Perform(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/service.DelayService/Perform",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(DelayServiceServer).Perform(ctx, req.(*empty.Empty))
	}
	return interceptor(ctx, in, info, handler)
}

var _DelayService_serviceDesc = grpc.ServiceDesc{
	ServiceName: "service.DelayService",
	HandlerType: (*DelayServiceServer)(nil),
	Methods: []grpc.MethodDesc{
		{
			MethodName: "perform",
			Handler:    _DelayService_Perform_Handler,
		},
	},
	Streams:  []grpc.StreamDesc{},
	Metadata: "service.proto",
}