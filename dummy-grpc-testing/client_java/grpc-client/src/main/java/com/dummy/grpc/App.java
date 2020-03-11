package com.dummy.grpc;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Optional;

import com.dummy.grpc.DelayServiceGrpc.DelayServiceBlockingStub;
import com.dummy.grpc.Service.ServiceResponse;
import com.google.protobuf.Empty;

public class App {

  public static void main(String[] args) {
    String serverAddress = System.getenv("SERVER_ADDRESS");
    ManagedChannel channel = ManagedChannelBuilder.forTarget(Optional.ofNullable(serverAddress).orElse("localhost:50051")).usePlaintext().build();
    DelayServiceBlockingStub delayServiceBlockingStub = DelayServiceGrpc.newBlockingStub(channel);
    ServiceResponse perform = delayServiceBlockingStub.perform(Empty.newBuilder().build());
    System.out.println(perform.getMessage());
  }
}
