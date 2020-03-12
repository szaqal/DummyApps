package com.dummy.grpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dummy.grpc.DelayServiceGrpc.DelayServiceBlockingStub;
import com.google.protobuf.Empty;

public class Caller implements Runnable {

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
