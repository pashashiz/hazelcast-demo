package com.ps.map;

import com.hazelcast.aggregation.Aggregator;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.ps.Utils.sleep;
import static com.ps.Utils.startNewNode;
import static java.util.Arrays.stream;

public class ExampleAggregation {

    public static void main(String[] args) {
        HazelcastInstance hz = startNewNode();
        String name = "node-" + hz.getCluster().getLocalMember().getAddress().getPort();
        Logger log = LoggerFactory.getLogger(name);
        IMap<String, String> test = hz.getMap("test");
        test.put(name + "-1", "hello world");
        test.put(name + "-2", "world is great");
        Map<String, Integer> result = test.aggregate(new WordFrequencyAggregator());
        log.info("Result: {}", result);
        sleep(10000);
        hz.shutdown();
    }

    public static class WordFrequencyAggregator extends Aggregator<Map.Entry<String, String>, Map<String, Integer>> {

        private final Map<String, Integer> words = new HashMap<>();

        @Override
        public void accumulate(Map.Entry<String, String> input) {
            stream(input.getValue().split("\\s"))
                    .forEach(word -> accumulateNext(word, 1));
        }

        @Override
        public void combine(Aggregator aggregator) {
            WordFrequencyAggregator other = ((WordFrequencyAggregator) aggregator);
            other.aggregate().forEach(this::accumulateNext);
        }

        private void accumulateNext(String word, Integer count) {
            words.compute(word, (existingWord, existingCount) -> {
                if (existingCount == null)
                    return count;
                else
                    return existingCount + count;
            });
        }

        @Override
        public Map<String, Integer> aggregate() {
            return words;
        }
    }
}
