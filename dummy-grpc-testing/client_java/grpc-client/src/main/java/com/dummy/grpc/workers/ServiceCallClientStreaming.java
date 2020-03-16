package com.dummy.grpc.workers;

import com.dummy.grpc.DelayServiceGrpc;
import com.dummy.grpc.Service;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

public class ServiceCallClientStreaming implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger("GRPC-ASYNC");

    private int jobId;

    private long start;

    private long end;

    StreamObserver<Empty> emptyStreamObserver ;

    public ServiceCallClientStreaming(int jobId, ManagedChannel managedChannel) {
        this.jobId = jobId;
        this.start = System.currentTimeMillis();

        emptyStreamObserver = DelayServiceGrpc.newStub(managedChannel).performClientStream(new StreamObserver<Service.ServiceResponse>() {

            @Override
            public void onNext(Service.ServiceResponse value) {
                LOG.info("Received {}", value.getMessage().toByteArray().length);
            }

            @Override
            public void onError(Throwable t) {
                LOG.info("ERROR", t);
            }

            @Override
            public void onCompleted() {
                LOG.info("COMPLETED");
            }
        });
    }


    @Override
    public void run() {
        Stream.of(Empty.newBuilder().build(), Empty.newBuilder().build(), Empty.newBuilder().build(), Empty.newBuilder().build())
                .forEach(emptyStreamObserver::onNext);
        emptyStreamObserver.onCompleted();
    }
}
