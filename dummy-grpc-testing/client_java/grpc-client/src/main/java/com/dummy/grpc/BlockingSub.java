package com.dummy.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.dummy.grpc.DelayServiceGrpc.DelayServiceBlockingStub;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class BlockingSub {

  private DelayServiceBlockingStub delayServiceBlockingStub;

  public BlockingSub() {

    ThreadFactory named = new ThreadFactoryBuilder().setNameFormat("transport").build();

    ExecutorService executorService = Executors.newFixedThreadPool(1, named);

    ManagedChannel channel = ManagedChannelBuilder
        .forTarget(Defaults.getServerAddress())
        .executor(executorService)
        .usePlaintext()
        .build();
    delayServiceBlockingStub = DelayServiceGrpc.newBlockingStub(channel);
  }

  public Runnable getCaller() {
    return new Caller(delayServiceBlockingStub);
  }


}
