package com.dummy.grpc.stub;

import com.dummy.grpc.Defaults;
import com.dummy.grpc.DelayServiceGrpc;
import com.dummy.grpc.DelayServiceGrpc.DelayServiceBlockingStub;
import com.dummy.grpc.workers.ServiceCallWorker;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BlockingStub {

  private DelayServiceBlockingStub delayServiceBlockingStub;

  public BlockingStub() {


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
