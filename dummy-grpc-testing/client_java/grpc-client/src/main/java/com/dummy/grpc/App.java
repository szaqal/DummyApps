package com.dummy.grpc;


import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

  private static final Logger log = LoggerFactory.getLogger("GRPC");

  public static void main(String[] args) {
    String serverAddress = System.getenv("SERVER_ADDRESS");
    BlockingSub blockingSub = new BlockingSub(serverAddress);

    ExecutorService executorService = Executors.newFixedThreadPool(5);
    for (int i = 0; i < 20; i++) {
      executorService.submit(blockingSub.getCaller());
    }

    Timer timer = new Timer();
    timer.schedule(new DumpThreadsTask(), 0, 5000);

  }
}
