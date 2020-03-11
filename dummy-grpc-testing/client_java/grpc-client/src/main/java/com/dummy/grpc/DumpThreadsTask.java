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
    ThreadInfo[] threadInfos = threadMxBean.dumpAllThreads(false, false);
    Set<Entry<State, List<ThreadInfo>>> entries = Arrays.stream(threadInfos).collect(Collectors.groupingBy(x -> x.getThreadState()))
        .entrySet();

    StringBuilder states = new StringBuilder();
    for(Entry<State, List<ThreadInfo>> s: entries) {

      states.append(String.format("\n\t %s : %s, %s, ", s.getKey(), s.getValue().size(),
          s.getValue().stream().map(x -> x.getThreadName()).collect(Collectors.joining(","))));
    }
    log.info("{}", states.toString());

  }
}
