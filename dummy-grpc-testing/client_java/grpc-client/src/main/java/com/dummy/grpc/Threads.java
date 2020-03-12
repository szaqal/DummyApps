package com.dummy.grpc;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class Threads {

  public static final ThreadFactory WORKER_FACTORY = new ThreadFactoryBuilder().setNameFormat("worker-%d").build();

  public static final ListeningExecutorService buildWorkerExecutor() {
    return MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(Defaults.threadPoolSize(), Threads.WORKER_FACTORY));
  }


}
