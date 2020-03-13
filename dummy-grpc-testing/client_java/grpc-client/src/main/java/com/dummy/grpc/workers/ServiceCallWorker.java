package com.dummy.grpc.workers;

import com.dummy.grpc.Defaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dummy.grpc.DelayServiceGrpc.DelayServiceBlockingStub;
import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;

public class ServiceCallWorker implements Runnable {

  private static final Logger log = LoggerFactory.getLogger("GRPC");

  private DelayServiceBlockingStub delayServiceBlockingStub;

  public ServiceCallWorker(DelayServiceBlockingStub delayServiceBlockingStub) {
    this.delayServiceBlockingStub = delayServiceBlockingStub;
  }

  @Override
  public void run() {
    int iterationsCount = Defaults.iterationsCount();
    for (int i = 0; i < iterationsCount; i++) {
      if (iterationsCount / 2 == i) {
        log.info("50% done");
      }
      ByteString message = delayServiceBlockingStub.perform(Empty.newBuilder().build()).getMessage();
      message.size();
    }
    log.info("100% done");
  }
}
