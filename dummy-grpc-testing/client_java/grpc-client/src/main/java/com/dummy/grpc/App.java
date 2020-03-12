package com.dummy.grpc;


import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.MoreExecutors;

public class App {


  public static void main(String[] args) {
    BlockingSub blockingSub = new BlockingSub();

    ExecutorService executorService = Executors.newFixedThreadPool(Defaults.threadPoolSize());
    for (int i = 0; i < Defaults.threadCount(); i++) {
      executorService.submit(blockingSub.getCaller());
    }

    Timer timer = new Timer();
    timer.schedule(new DumpThreadsTask(), 0, 15000);

  }
}
