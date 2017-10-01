package com.ps;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.Collections;

public class Utils {

    public static HazelcastInstance startNewNode() {
        Config config = new Config().
                setProperty("hazelcast.logging.type", "slf4j").
                setProperty("hazelcast.phone.home.enabled", "false");
        JoinConfig join = config.getNetworkConfig().getJoin();
        join.getAwsConfig().setEnabled(false);
        join.getMulticastConfig().setEnabled(false);
        join.getTcpIpConfig().setEnabled(true)
                .setMembers(Collections.singletonList("localhost"));
        return Hazelcast.newHazelcastInstance(config);
    }

    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sleepUpTo(int milliseconds) {
        sleep((int) (1000 *  Math.random()));
    }

}
