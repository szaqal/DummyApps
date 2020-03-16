package com.dummy.grpc.workers.stream;

import com.dummy.grpc.Defaults;
import com.dummy.grpc.DelayServiceGrpc;
import com.dummy.grpc.Service;
import com.google.common.primitives.Bytes;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import org.jasypt.util.binary.BasicBinaryEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

import static com.dummy.grpc.Defaults.MEGABYTE;

public class ServiceCallClientStreamingSingleRequest implements Runnable {

    private BasicBinaryEncryptor binaryEncryptor = new BasicBinaryEncryptor();

    private static final Logger log = LoggerFactory.getLogger("GRPC-ASYNC");

    private int jobId;

    private long start;

    private long end;

    private String summary;


    StreamObserver<Empty> emptyStreamObserver;

    DelayServiceGrpc.DelayServiceStub delayServiceStub;

    public ServiceCallClientStreamingSingleRequest(int jobId, ManagedChannel managedChannel) {
        this.jobId = jobId;
        this.start = System.currentTimeMillis();
        delayServiceStub = DelayServiceGrpc.newStub(managedChannel);
    }

    private class CustomStreamObserver implements StreamObserver<Service.ServiceResponse> {

        CountDownLatch finishLatch = new CountDownLatch(Defaults.iterationsCount() - 1);

        byte[] data = new byte[]{};

        @Override
        public void onNext(Service.ServiceResponse value) {
            synchronized (this) {
                byte[] bytes = value.getMessage().toByteArray();
                data = Bytes.concat(data, bytes);
            }

        }

        @Override
        public void onError(Throwable t) {
            log.info("ERROR", t);
        }

        @Override
        public void onCompleted() {
            finishLatch.countDown();
        }
    }


    @Override
    public void run() {

        CustomStreamObserver responseObserver = new CustomStreamObserver();
        for (int i = 0; i < Defaults.iterationsCount(); i++) {

            emptyStreamObserver = delayServiceStub.performClientStream(responseObserver);
            emptyStreamObserver.onNext(Empty.newBuilder().build());
            emptyStreamObserver.onCompleted();
        }

        try {
            responseObserver.finishLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long encryptionElapsed = 0;// encryptData(responseObserver.data);
        end = System.currentTimeMillis();
        summary = String.format("Finished / time %s ms / encryption %s ms / received %s MB", end - start, encryptionElapsed, responseObserver.data.length / MEGABYTE);

        log.info("[{}] {}", toString(), summary);
    }

    private long encryptData(byte[] data) {
        long start = System.currentTimeMillis();
        binaryEncryptor.setPassword("pass");
        binaryEncryptor.encrypt(data);
        return System.currentTimeMillis() - start;
    }

    @Override
    public String toString() {
        return "job-" + jobId;
    }
}
