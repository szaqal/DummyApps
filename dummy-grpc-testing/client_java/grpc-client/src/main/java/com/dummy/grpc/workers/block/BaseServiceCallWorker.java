package com.dummy.grpc.workers.block;

import com.dummy.grpc.Defaults;
import com.google.common.primitives.Bytes;
import org.jasypt.util.binary.BasicBinaryEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.dummy.grpc.Defaults.MEGABYTE;

public abstract class BaseServiceCallWorker implements Runnable {

    private BasicBinaryEncryptor binaryEncryptor = new BasicBinaryEncryptor();

    protected static final Logger log = LoggerFactory.getLogger("GRPC");

    private int jobId;

    private long start;

    private long end;

    public BaseServiceCallWorker(int jobId) {
        this.jobId = jobId;
        this.start = System.currentTimeMillis();
    }


    @Override
    public String toString() {
        return "job-" + jobId;
    }

    protected abstract byte[] getBytes();

    @Override
    public void run() {
        int iterationsCount = Defaults.iterationsCount();
        byte[] data = new byte[]{};

        for (int i = 0; i < iterationsCount; i++) {
            data = Bytes.concat(data, getBytes());
        }

        long encryptionElapsed = encryptData(data);
        end = System.currentTimeMillis();

        log.info("{} Finished / time {} ms / encryption {} ms / received {} MB", toString(), end - start, encryptionElapsed, data.length / MEGABYTE);
    }

    private long encryptData(byte[] data) {
        long start = System.currentTimeMillis();
        binaryEncryptor.setPassword("pass");
        binaryEncryptor.encrypt(data);
        return System.currentTimeMillis() - start;
    }
}
