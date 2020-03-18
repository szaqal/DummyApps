package com.dummy.grpc.workers.bistream;

import com.dummy.grpc.Defaults;
import com.dummy.grpc.DelayServiceGrpc;
import com.dummy.grpc.Service;
import com.google.common.primitives.Bytes;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

import static com.dummy.grpc.Defaults.MEGABYTE;

public class BiStreamWorker implements Runnable {

    private static final Logger log = LoggerFactory.getLogger("GRPC-BI-ASYNC");

    private int jobId;

    private long start;

    private long end;


    private DelayServiceGrpc.DelayServiceStub delayServiceStub;

    public BiStreamWorker(int jobId, ManagedChannel managedChannel) {
        this.jobId = jobId;
        this.start = System.currentTimeMillis();
        delayServiceStub = DelayServiceGrpc.newStub(managedChannel);
    }


    @Override
    public void run() {


        final CountDownLatch finishLatch = new CountDownLatch(1);

        StreamObserver<Empty> requestObserver = delayServiceStub.performBiStream(new StreamObserver<Service.ServiceResponse>() {

            byte[] data = new byte[]{};

            String summary = "";

            @Override
            public void onNext(Service.ServiceResponse value) {
                data = Bytes.concat(data, value.getMessage().toByteArray());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                end = System.currentTimeMillis();
                finishLatch.countDown();
                summary = String.format("Finished / time %s ms / encryption %s ms / received %s MB", end - start, 0, data.length / MEGABYTE);
                log.info("[{}] {}", BiStreamWorker.this.toString(), summary);
            }
        });

        try {
            for (int i = 0; i < Defaults.iterationsCount(); i++) {
                requestObserver.onNext(Empty.newBuilder().build());
            }
        } catch (RuntimeException e) {
            requestObserver.onError(e);
            throw e;
        }

        requestObserver.onCompleted();
        try {
            finishLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    @Override
    public String toString() {
        return "job-" + jobId;
    }
}
