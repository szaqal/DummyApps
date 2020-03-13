package com.dummy.grpc.threads;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import com.dummy.grpc.Defaults;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Threads {

    private static final Logger LOG = LoggerFactory.getLogger("GRPC");

    private static AtomicInteger DISCARDED = new AtomicInteger();

    public static final ThreadFactory WORKER_FACTORY = new ThreadFactoryBuilder().setNameFormat("worker-%d").build();

    public static final ListeningExecutorService buildWorkerExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                Defaults.threadPoolSize(),
                Defaults.threadPoolSize(),
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(10),
                WORKER_FACTORY);
        executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                LOG.info("Job discarded [{}] / Jobs discarded [{}]", r.toString(), DISCARDED.addAndGet(1));

            }
        });
        return MoreExecutors.listeningDecorator(executor);
    }


}
