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

    private long encryptionElapsed;

    private String summary;

    byte[] data = new byte[]{};

    private CountDownLatch finishLatch = new CountDownLatch(Defaults.iterationsCount()-1);

    StreamObserver<Empty> emptyStreamObserver ;

    DelayServiceGrpc.DelayServiceStub delayServiceStub;

    public ServiceCallClientStreamingSingleRequest(int jobId, ManagedChannel managedChannel) {
        this.jobId = jobId;
        this.start = System.currentTimeMillis();
        delayServiceStub = DelayServiceGrpc.newStub(managedChannel);
    }




    @Override
    public void run() {

        Empty[] empties = new Empty[]{Empty.newBuilder().build()};
        for (int i = 0; i <  Defaults.iterationsCount(); i++) {

            emptyStreamObserver = delayServiceStub.performClientStream(new StreamObserver<Service.ServiceResponse>() {

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
                    finishLatch.countDown();
                }

            });

            Stream.of(empties).forEach(emptyStreamObserver::onNext);
            emptyStreamObserver.onCompleted();
        }

        try {
            finishLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long encryptionElapsed = encryptData(data);
        end = System.currentTimeMillis();
        summary =  String.format("Finished / time %s ms / encryption %s ms / received %s MB",  end - start, encryptionElapsed, data.length / MEGABYTE);

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
