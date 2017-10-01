package com.ps.map;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ps.Utils.sleep;
import static com.ps.Utils.startNewNode;
import static java.util.stream.IntStream.range;

public class ExampleKeyValue {

    public static void main(String[] args) throws Exception {
        HazelcastInstance hz = startNewNode();
        String name = "node-" + hz.getCluster().getLocalMember().getAddress().getPort();
        Logger log = LoggerFactory.getLogger(name);
        IMap<String, String> test = hz.getMap("test");
        range(1, Integer.MAX_VALUE).forEach(counter -> {
            log.info("Map: " + test.getAll(test.keySet()));
            test.put(name, "hey-" + counter);
            sleep(1000);
        });
        hz.shutdown();
    }
}
