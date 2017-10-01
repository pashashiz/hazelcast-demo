package com.ps.queue;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ps.Utils.startNewNode;

public class ExampleConsumer {

    public static void main(String[] args) throws Exception {
        HazelcastInstance hz = startNewNode();
        String name = "node-" + hz.getCluster().getLocalMember().getAddress().getPort();
        Logger log = LoggerFactory.getLogger(name);
        IQueue<String> queue = hz.getQueue("requests");
        while (true) {
            String request = queue.take();
            log.info("Processing {}", request);
        }
    }

}
