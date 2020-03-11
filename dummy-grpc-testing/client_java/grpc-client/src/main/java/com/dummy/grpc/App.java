package com.dummy.grpc;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import com.dummy.grpc.DelayServiceGrpc.DelayServiceBlockingStub;
import com.dummy.grpc.Service.ServiceResponse;
import com.google.protobuf.Empty;

public class App {

  public static void main(String[] args) {
    ManagedChannel channel = ManagedChannelBuilder.forTarget(System.getenv("SERVER_ADDRESS")).usePlaintext().build();
    DelayServiceBlockingStub delayServiceBlockingStub = DelayServiceGrpc.newBlockingStub(channel);
    ServiceResponse perform = delayServiceBlockingStub.perform(Empty.newBuilder().build());
    System.out.println(perform.getMessage());
  }
}
