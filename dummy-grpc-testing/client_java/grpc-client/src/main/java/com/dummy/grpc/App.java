package com.dummy.grpc;


import com.dummy.grpc.threads.Threads;
import com.dummy.grpc.workers.bistream.BiStreamWorker;
import com.dummy.grpc.workers.block.ServiceCallBlockingWorker;
import com.dummy.grpc.workers.stream.ClientStreamingWorker;
import com.dummy.grpc.workers.stream.StreamingSingleRequestWorker;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class App {

  private static final Logger LOG = LoggerFactory.getLogger("MAIN");

  public static void main(String[] args) throws InterruptedException {

    ManagedChannel channel = ManagedChannelBuilder
            .forTarget(Defaults.getServerAddress())
            .maxInboundMessageSize(Integer.MAX_VALUE)
            .usePlaintext()
            .build();


    ExecutorService workerExecutor = Threads.buildWorkerExecutor();
    for (int i = 1; i < Defaults.threadCount() +1; i++) {
      Runnable caller = null;
      switch (Defaults.callerType()) {
        case "blocking": caller = getBlockingServiceWorker(channel, i); break;
        case "stream": caller =  getClientStreamWorker(channel, i); break;
        case "streamsingle": caller = getServiceCallClientStreamingSingleRequest(channel, i); break;
        case "bistream": caller = getBiStreamWorker(channel, i); break;
        default: LOG.error("Unknown caller type {}" , Defaults.callerType()); break;
      }
      //Runnable caller = getFutureServiceWorker(channel, i);
      workerExecutor.submit(caller);
      Thread.sleep(5000/i);
    }

    try {
      workerExecutor.shutdown();
      if (!workerExecutor.awaitTermination(1, TimeUnit.HOURS)) {
        workerExecutor.shutdownNow();
      }
    } catch (InterruptedException e) {
      workerExecutor.shutdownNow();
    }  }

  private static Runnable getBlockingServiceWorker(ManagedChannel managedChannel, int jobId) {
    return new ServiceCallBlockingWorker(jobId, managedChannel);
  }


  private static Runnable getBiStreamWorker(ManagedChannel managedChannel, int jobId) {
    return new BiStreamWorker(jobId, managedChannel);
  }

  private static Runnable getFutureServiceWorker(ManagedChannel managedChannel, int jobId) {
    return new ServiceCallBlockingWorker(jobId, managedChannel);
  }

  private static Runnable getClientStreamWorker(ManagedChannel managedChannel, int jobId) {
    return new ClientStreamingWorker(jobId, managedChannel);
  }

  private static Runnable getServiceCallClientStreamingSingleRequest(ManagedChannel managedChannel, int jobId) {
    return new StreamingSingleRequestWorker(jobId, managedChannel);
  }
}
