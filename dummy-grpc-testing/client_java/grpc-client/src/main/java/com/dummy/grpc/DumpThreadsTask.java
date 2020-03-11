package com.dummy.grpc;

import java.lang.Thread.State;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimerTask;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DumpThreadsTask extends TimerTask {

  private static final Logger log = LoggerFactory.getLogger("Stats");

  @Override
  public void run() {
    ThreadMXBean threadMxBean = ManagementFactory.getThreadMXBean();
    ThreadInfo[] threadInfos = threadMxBean.dumpAllThreads(true, true);
    Set<Entry<State, List<ThreadInfo>>> entries = Arrays.stream(threadInfos).collect(Collectors.groupingBy(x -> x.getThreadState()))
        .entrySet();

    StringBuilder builder = new StringBuilder();
    for(Entry<State, List<ThreadInfo>> s: entries) {
      builder.append(String.format(" %s : %s ", s.getKey(), s.getValue().size()));
    }
    log.info("{}", builder.toString());

  }
}