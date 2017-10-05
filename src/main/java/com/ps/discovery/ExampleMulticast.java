package com.ps.discovery;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.core.Hazelcast;

import java.util.Collections;

public class ExampleMulticast {

    public static void main(String[] args) {
        // NOTE 1: enable multicast on unix "sudo tcpdump -ni en0 host 228.0.0.4"
        // NOTE 2: use "-Djava.net.preferIPv4Stack=true" when run
        Config config = new Config().setProperty("hazelcast.logging.type", "slf4j");
        JoinConfig join = config.getNetworkConfig().getJoin();
        join.getAwsConfig().setEnabled(false);
        join.getMulticastConfig().setEnabled(true)
                .setMulticastGroup("228.0.0.4").setMulticastPort(54327);
        join.getTcpIpConfig().setEnabled(false);
        Hazelcast.newHazelcastInstance(config);
    }
}
