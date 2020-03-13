package com.dummy.grpc.workers;

import com.dummy.grpc.Defaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dummy.grpc.DelayServiceGrpc.DelayServiceBlockingStub;
import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;

import java.util.concurrent.TimeUnit;

public class ServiceCallWorker implements Runnable {

  private static final Logger log = LoggerFactory.getLogger("GRPC");

  private DelayServiceBlockingStub delayServiceBlockingStub;

  private int jobId;

  private long start;

  private long end;


  public ServiceCallWorker(int jobId, DelayServiceBlockingStub delayServiceBlockingStub) {
    this.delayServiceBlockingStub = delayServiceBlockingStub;
    this.jobId = jobId;
    this.start = System.currentTimeMillis();
  }

  @Override
  public void run() {
    int iterationsCount = Defaults.iterationsCount();
    for (int i = 0; i < iterationsCount; i++) {
      if (iterationsCount / 2 == i) {
        log.debug("{} 50% done", toString());
      }
      ByteString message = delayServiceBlockingStub.perform(Empty.newBuilder().build()).getMessage();
      message.size();
    }
    end = System.currentTimeMillis();
    log.info("{} 100% done in {} ms", toString(),end-start);
  }

  @Override
  public String toString() {
    return "job-"+jobId;
  }
}
