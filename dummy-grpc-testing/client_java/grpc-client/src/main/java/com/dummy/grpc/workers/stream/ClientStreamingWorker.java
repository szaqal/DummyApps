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
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

import static com.dummy.grpc.Defaults.MEGABYTE;

public class ClientStreamingWorker implements Runnable {

    private BasicBinaryEncryptor binaryEncryptor = new BasicBinaryEncryptor();

    private static final Logger log = LoggerFactory.getLogger("GRPC-ASYNC");

    private int jobId;

    private long start;

    private long end;

    private long encryptionElapsed;

    private String summary;

    final CountDownLatch finishLatch = new CountDownLatch(1);

    StreamObserver<Empty> emptyStreamObserver ;

    public ClientStreamingWorker(int jobId, ManagedChannel managedChannel) {
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
                encryptionElapsed = 0;//encryptData(data);
                end = System.currentTimeMillis();
                summary =  String.format("Finished / time %s ms / encryption %s ms / received %s MB",  end - start, encryptionElapsed, data.length / MEGABYTE);
                finishLatch.countDown();
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
        try {
            finishLatch.await();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("[{}] {}", toString(), summary);
    }

    @Override
    public String toString() {
        return "job-" + jobId;
    }
}

