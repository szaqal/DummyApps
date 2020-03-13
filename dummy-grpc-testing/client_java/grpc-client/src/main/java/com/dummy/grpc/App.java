package com.dummy.grpc;


import com.dummy.grpc.threads.Threads;
import com.dummy.grpc.workers.ServiceCallWorker;
import com.google.protobuf.Empty;
import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

public class App {
  private static final Logger LOG = LoggerFactory.getLogger("MAIN");

  public static void main(String[] args) throws InterruptedException {
    //Timer timer = new Timer();
    //timer.schedule(new DumpThreadsTask(), 0, 10_000);

    ManagedChannel channel = ManagedChannelBuilder
            .forTarget(Defaults.getServerAddress())
            .usePlaintext()
            .build();


    ExecutorService workerExecutor = Threads.buildWorkerExecutor();
    for (int i = 1; i < Defaults.threadCount() +1; i++) {
      Runnable caller = getBlockingServiceWorker(channel, i);
      LOG.info("Submit Job [{}]", caller);
      workerExecutor.submit(caller);
      Thread.sleep(5000/i);
    }
  }

  private static Runnable getBlockingServiceWorker(ManagedChannel managedChannel, int jobId) {
    return new ServiceCallWorker(jobId, () -> DelayServiceGrpc
            .newBlockingStub(managedChannel)
            .perform(Empty.newBuilder().build()).getMessage());
  }

}
