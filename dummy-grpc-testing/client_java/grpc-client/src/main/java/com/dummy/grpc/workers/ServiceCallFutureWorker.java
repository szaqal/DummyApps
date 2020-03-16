package com.dummy.grpc.workers;

import com.dummy.grpc.DelayServiceGrpc;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;

import java.util.concurrent.ExecutionException;

public class ServiceCallFutureWorker extends BaseServiceCallWorker implements Runnable {

    private DelayServiceGrpc.DelayServiceFutureStub delayServiceFutureStub;

    public ServiceCallFutureWorker(int jobId, ManagedChannel managedChannel) {

        super(jobId);
        if (delayServiceFutureStub == null) {
            delayServiceFutureStub = DelayServiceGrpc.newFutureStub(managedChannel);
        }
    }

    @Override
    protected byte[] getBytes() {
        try {
            return delayServiceFutureStub.perform(Empty.newBuilder().build()).get().getMessage().toByteArray();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
