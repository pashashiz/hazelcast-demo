package com.ps.others;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ILock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ps.Utils.sleep;
import static com.ps.Utils.sleepUpTo;
import static com.ps.Utils.startNewNode;
import static java.util.stream.IntStream.range;

public class ExampleLock {

    public static void main(String[] args) throws Exception {
        HazelcastInstance hz = startNewNode();
        String name = "node-" + hz.getCluster().getLocalMember().getAddress().getPort();
        Logger log = LoggerFactory.getLogger(name);
        ILock mutex = hz.getLock("test");
        while (true) {
            log.info("Waiting fo a lock...");
            mutex.lock();
            try {
                log.info("Doing very important job alone...");
                range(1, 11).forEach(i -> {
                    log.info("{}%", i * 10);
                    sleep(500);
                });
                log.info("Done");
            } finally {
                mutex.unlock();
            }
            sleepUpTo(1000);
        }
    }
}
