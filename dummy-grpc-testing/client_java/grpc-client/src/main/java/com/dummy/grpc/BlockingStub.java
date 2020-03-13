package com.dummy.grpc;

import com.dummy.grpc.DelayServiceGrpc.DelayServiceBlockingStub;
import com.dummy.grpc.workers.ServiceCallWorker;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.ThreadFactory;

public class BlockingStub {

  private DelayServiceBlockingStub delayServiceBlockingStub;

  public BlockingStub() {

    ThreadFactory named = new ThreadFactoryBuilder().setNameFormat("transport").build();


    ManagedChannel channel = ManagedChannelBuilder
        .forTarget(Defaults.getServerAddress())
        .usePlaintext()
        .build();
    delayServiceBlockingStub = DelayServiceGrpc.newBlockingStub(channel);
  }

  public Runnable getCaller(int jobId) {
    return new ServiceCallWorker(jobId, delayServiceBlockingStub);
  }


}
