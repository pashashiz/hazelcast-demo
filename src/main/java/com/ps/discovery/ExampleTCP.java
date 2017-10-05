package com.ps.discovery;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.core.Hazelcast;

import java.util.Collections;

public class ExampleTCP {

    public static void main(String[] args) {
        Config config = new Config().setProperty("hazelcast.logging.type", "slf4j");
        JoinConfig join = config.getNetworkConfig().getJoin();
        join.getAwsConfig().setEnabled(false);
        join.getMulticastConfig().setEnabled(false);
        join.getTcpIpConfig().setEnabled(true)
                .setMembers(Collections.singletonList("localhost"));
        Hazelcast.newHazelcastInstance(config);
    }
}
