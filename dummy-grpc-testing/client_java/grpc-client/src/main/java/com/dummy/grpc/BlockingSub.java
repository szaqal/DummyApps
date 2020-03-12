package com.dummy.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dummy.grpc.DelayServiceGrpc.DelayServiceBlockingStub;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.protobuf.Empty;

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

  public static class Caller implements Runnable {

    private static final Logger log = LoggerFactory.getLogger("GRPC");

    private DelayServiceBlockingStub delayServiceBlockingStub;

    public Caller(DelayServiceBlockingStub delayServiceBlockingStub) {
      this.delayServiceBlockingStub = delayServiceBlockingStub;
    }

    @Override
    public void run() {
      int iterationsCount = Defaults.iterationsCount();
      for (int i = 0; i < iterationsCount; i++) {
        if (iterationsCount / 2 == i) {
          log.info("50% done");
        }
        delayServiceBlockingStub.perform(Empty.newBuilder().build());
      }
      log.info("100% done");
    }
  }
}
