package com.ps.map;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ps.Utils.sleep;
import static com.ps.Utils.startNewNode;
import static java.util.stream.IntStream.range;

public class ExampleKeyValue {

    // - All operations are thread safe
    // - We can use a Map as a Cache with TTL
    // - We can use persistence storage to survive cluster restart
    // - We can use data affinity
    // - We can query data
    // - Or aggregate (map-reduce)
    // - We can lock Map items
    // - We can perform atomic operations
    // - We can listen to any changes
    // - And others...
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
