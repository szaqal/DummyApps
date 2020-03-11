package com.dummy.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dummy.grpc.DelayServiceGrpc.DelayServiceBlockingStub;
import com.google.protobuf.Empty;

public class BlockingSub {

  private DelayServiceBlockingStub delayServiceBlockingStub;

  public BlockingSub(String serverAddress) {
    ManagedChannel channel = ManagedChannelBuilder.forTarget(Optional.ofNullable(serverAddress).orElse("localhost:50051")).usePlaintext()
        .build();
    delayServiceBlockingStub = DelayServiceGrpc.newBlockingStub(channel);
  }

  public Runnable getCaller() {
    return new Caller(delayServiceBlockingStub);
  }

  public static class Caller implements Runnable {

    private static final Logger log = LoggerFactory.getLogger("GRPC");

    private DelayServiceBlockingStub delayServiceBlockingStub;

    public Caller(DelayServiceBlockingStub delayServiceBlockingStub) {
      this.delayServiceBlockingStub = delayServiceBlockingStub;
    }

    @Override
    public void run() {
      for (int i = 0; i < 200; i++) {
        delayServiceBlockingStub.perform(Empty.newBuilder().build());
      }
      log.info(this + " DONE");
    }
  }
}
