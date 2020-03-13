package com.dummy.grpc.workers;

import com.dummy.grpc.Defaults;
import com.dummy.grpc.DelayServiceGrpc.DelayServiceBlockingStub;
import com.google.common.primitives.Bytes;
import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import org.jasypt.util.binary.BasicBinaryEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class ServiceCallWorker implements Runnable {

  private static final Logger log = LoggerFactory.getLogger("GRPC");

  private Supplier<ByteString> dataSupplier;

  private BasicBinaryEncryptor binaryEncryptor = new BasicBinaryEncryptor();

  private int jobId;

  private long start;

  private long end;

  private static final long  MEGABYTE = 1024L * 1024L;

  public ServiceCallWorker(int jobId, Supplier<ByteString> dataSupplier) {
    this.dataSupplier = dataSupplier;
    this.jobId = jobId;
    this.start = System.currentTimeMillis();
  }

  @Override
  public void run() {
    int iterationsCount = Defaults.iterationsCount();
    byte[] data = new byte[]{};

    for (int i = 0; i < iterationsCount; i++) {
      data = Bytes.concat(data, dataSupplier.get().toByteArray());
    }

    long encryptionElapsed = encryptData(data);
    end = System.currentTimeMillis();

    log.info("{} 100% / time {} ms / encryption {} ms / received {} MB", toString(), end - start, encryptionElapsed, data.length / MEGABYTE);
  }

  private long encryptData(byte[] data) {
    long start = System.currentTimeMillis();
      binaryEncryptor.setPassword("pass");
    for (int i=0;i<5;i++) {
        binaryEncryptor.encrypt(data);
    }
    return System.currentTimeMillis() - start;
  }

  @Override
  public String toString() {
    return "job-"+jobId;
  }
}
