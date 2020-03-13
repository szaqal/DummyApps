package com.dummy.grpc;


import com.dummy.grpc.threads.Threads;
import com.google.common.util.concurrent.ListeningExecutorService;

public class App {

  public static void main(String[] args) {
    //Timer timer = new Timer();
    //timer.schedule(new DumpThreadsTask(), 0, 10_000);

    BlockingStub blockingSub = new BlockingStub();

    ListeningExecutorService workerExecutor = Threads.buildWorkerExecutor();
    for (int i = 0; i < Defaults.threadCount(); i++) {
      workerExecutor.submit(blockingSub.getCaller());
    }

  }
}
