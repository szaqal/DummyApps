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

import java.util.Arrays;
import java.util.stream.Stream;

import static com.dummy.grpc.Defaults.MEGABYTE;

public class ServiceCallClientStreaming implements Runnable {

    private BasicBinaryEncryptor binaryEncryptor = new BasicBinaryEncryptor();

    private static final Logger log = LoggerFactory.getLogger("GRPC-ASYNC");

    private int jobId;

    private long start;

    private long end;

    StreamObserver<Empty> emptyStreamObserver ;

    public ServiceCallClientStreaming(int jobId, ManagedChannel managedChannel) {
        this.jobId = jobId;
        this.start = System.currentTimeMillis();

        emptyStreamObserver = DelayServiceGrpc.newStub(managedChannel).performClientStream(new StreamObserver<Service.ServiceResponse>() {

            byte[] data = new byte[]{};

            @Override
            public void onNext(Service.ServiceResponse value) {
                data = Bytes.concat(data, value.getMessage().toByteArray());
            }

            @Override
            public void onError(Throwable t) {
                log.info("ERROR", t);
            }

            @Override
            public void onCompleted() {
                long encryptionElapsed = encryptData(data);
                end = System.currentTimeMillis();
                log.info("{} 100% / time {} ms / encryption {} ms / received {} MB", toString(), end - start, encryptionElapsed, data.length / MEGABYTE);
            }


            private long encryptData(byte[] data) {
                long start = System.currentTimeMillis();
                binaryEncryptor.setPassword("pass");
                binaryEncryptor.encrypt(data);
                return System.currentTimeMillis() - start;
            }
        });
    }


    @Override
    public void run() {
        Empty[] empties = new Empty[Defaults.iterationsCount()];
        Arrays.fill(empties, Empty.newBuilder().build());
        Stream.of(empties).forEach(emptyStreamObserver::onNext);
        emptyStreamObserver.onCompleted();
        log.info("Completed");
    }

    @Override
    public String toString() {
        return "job-" + jobId;
    }
}
