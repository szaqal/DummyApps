package com.dummy.grpc.workers;

import com.dummy.grpc.DelayServiceGrpc;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;

public class ServiceCallBlockingWorker extends BaseServiceCallWorker implements Runnable {


    private static DelayServiceGrpc.DelayServiceBlockingStub delayServiceBlockingStub;


    public ServiceCallBlockingWorker(int jobId, ManagedChannel managedChannel) {
        super(jobId);

        if (delayServiceBlockingStub == null) {
            delayServiceBlockingStub = DelayServiceGrpc
                    .newBlockingStub(managedChannel);
        }

    }


    @Override
    protected byte[] getBytes() {
        return delayServiceBlockingStub.perform(Empty.newBuilder().build()).getMessage().toByteArray();
    }
}
