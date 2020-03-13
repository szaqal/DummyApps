package com.dummy.grpc;


import com.dummy.grpc.threads.Threads;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
  private static final Logger LOG = LoggerFactory.getLogger("MAIN");

  public static void main(String[] args) throws InterruptedException {
    //Timer timer = new Timer();
    //timer.schedule(new DumpThreadsTask(), 0, 10_000);


    ExecutorService workerExecutor = Threads.buildWorkerExecutor();
    for (int i = 1; i < Defaults.threadCount() +1; i++) {
      Runnable caller = new BlockingStub().getCaller(i);
      LOG.info("Submit Job [{}]", caller);
      workerExecutor.submit(caller);
      Thread.sleep(5000/i);
    }

  }
}
