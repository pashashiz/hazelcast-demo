package com.ps.topic;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ps.Utils.sleep;
import static com.ps.Utils.startNewNode;

public class ExamplePong {

    public static void main(String[] args) throws Exception {
        HazelcastInstance hz = startNewNode();
        String name = "node-" + hz.getCluster().getLocalMember().getAddress().getPort();
        Logger log = LoggerFactory.getLogger(name);
        ITopic<String> ping = hz.getTopic("ping");
        ITopic<String> pong = hz.getTopic("pong");
        ping.addMessageListener(message -> {
            log.info("Got a message: " + message.getMessageObject());
            sleep(1000);
            pong.publish("Ping...");
        });
    }
}
