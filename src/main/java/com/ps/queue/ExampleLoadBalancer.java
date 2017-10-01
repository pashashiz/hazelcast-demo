package com.ps.queue;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ps.Utils.sleep;
import static com.ps.Utils.startNewNode;
import static java.util.stream.IntStream.range;

public class ExampleLoadBalancer {

    public static void main(String[] args) {
        HazelcastInstance hz = startNewNode();
        String name = "node-" + hz.getCluster().getLocalMember().getAddress().getPort();
        Logger log = LoggerFactory.getLogger(name);
        IQueue<String> queue = hz.getQueue("requests");
        range(1, Integer.MAX_VALUE).forEach(counter -> {
            log.info("Got request {}, forwarding...", counter);
            queue.add("request-" + counter);
            sleep(1000);
        });
        hz.shutdown();
    }

}
