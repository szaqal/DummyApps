package com.dummy.grpc;


import com.dummy.grpc.threads.Threads;
import com.dummy.grpc.workers.ServiceCallWorker;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
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
      Runnable caller = getOtherWorker(channel, i);
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

  private static Runnable getFutureServiceWorker(ManagedChannel managedChannel, int jobId) {
      return new ServiceCallWorker(jobId,()-> {
          try {
              return DelayServiceGrpc.
                      newFutureStub(managedChannel)
                      .perform(Empty.newBuilder().build())
                      .get().getMessage();
          } catch (InterruptedException | ExecutionException e) {
              e.printStackTrace();
          }
          return null;
      });

  }

  private static Runnable getOtherWorker(ManagedChannel managedChannel, int jobId) {
      DelayServiceGrpc.newStub(managedChannel).perform(Empty.newBuilder().build(), new StreamObserver<Service.ServiceResponse>() {

          @Override
          public void onNext(Service.ServiceResponse value) {
              value.getMessage();
          }

          @Override
          public void onError(Throwable t) {

          }

          @Override
          public void onCompleted() {
              LOG.info("Completed");
          }
      });
  }
}
