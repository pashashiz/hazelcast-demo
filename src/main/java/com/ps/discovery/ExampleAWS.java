package com.ps.discovery;

import com.hazelcast.config.AwsConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.core.Hazelcast;

import java.util.Collections;

public class ExampleAWS {

    public static void main(String[] args) {
        Config config = new Config().setProperty("hazelcast.logging.type", "slf4j");
        JoinConfig join = config.getNetworkConfig().getJoin();
        AwsConfig awsConfig = new AwsConfig().setEnabled(true);
//        config.setAccessKey( ... ) ;
//        config.setSecretKey( ... );
//        config.setRegion( ... );
//        config.setSecurityGroupName( ... );
//        config.setTagKey( ... );
//        config.setTagValue( ... );
//        config.setEnabled(true);
        join.setAwsConfig(awsConfig);
        join.getMulticastConfig().setEnabled(false);
        join.getTcpIpConfig().setEnabled(false);
    }
}
