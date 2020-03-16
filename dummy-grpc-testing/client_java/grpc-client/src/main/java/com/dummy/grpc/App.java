package com.dummy.grpc;


import com.dummy.grpc.threads.Threads;
import com.dummy.grpc.workers.ServiceCallBlockingWorker;
import com.dummy.grpc.workers.ServiceCallClientStreaming;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

public class App {

  private static final Logger LOG = LoggerFactory.getLogger("MAIN");

  public static void main(String[] args) throws InterruptedException {

    ManagedChannel channel = ManagedChannelBuilder
            .forTarget(Defaults.getServerAddress())
            .usePlaintext()
            .build();


    ExecutorService workerExecutor = Threads.buildWorkerExecutor();
    for (int i = 1; i < Defaults.threadCount() +1; i++) {
      //Runnable caller = getBlockingServiceWorker(channel, i);
      //Runnable caller = getFutureServiceWorker(channel, i);
      Runnable caller = getClientStream(channel, i);
      LOG.info("Submit Job [{}]", caller);
      workerExecutor.submit(caller);
      Thread.sleep(5000/i);
    }
  }

  private static Runnable getBlockingServiceWorker(ManagedChannel managedChannel, int jobId) {
    return new ServiceCallBlockingWorker(jobId, managedChannel);
  }

  private static Runnable getFutureServiceWorker(ManagedChannel managedChannel, int jobId) {
    return new ServiceCallBlockingWorker(jobId, managedChannel);
  }

  private static Runnable getClientStream(ManagedChannel managedChannel, int jobId) {
    return new ServiceCallClientStreaming(jobId, managedChannel);
  }

}
