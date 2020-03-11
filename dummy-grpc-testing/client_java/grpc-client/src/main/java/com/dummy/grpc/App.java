package com.dummy.grpc;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Optional;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dummy.grpc.DelayServiceGrpc.DelayServiceBlockingStub;

public class App {

  private static final Logger log = LoggerFactory.getLogger("GRPC");

  public static void main(String[] args) {
    String serverAddress = System.getenv("SERVER_ADDRESS");
    ManagedChannel channel = ManagedChannelBuilder.forTarget(Optional.ofNullable(serverAddress).orElse("localhost:50051")).usePlaintext()
        .build();
    DelayServiceBlockingStub delayServiceBlockingStub = DelayServiceGrpc.newBlockingStub(channel);

    ExecutorService executorService = Executors.newFixedThreadPool(5);
    for (int i = 0; i < 50; i++) {
      executorService.submit(new Caller(delayServiceBlockingStub));
    }

    Timer timer = new Timer();
    timer.schedule(new DumpThreadsTask(), 0, 5000);

  }
}
