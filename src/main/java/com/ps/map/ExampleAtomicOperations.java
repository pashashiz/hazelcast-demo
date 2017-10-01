package com.ps.map;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Objects;

import static com.ps.Utils.sleepUpTo;
import static com.ps.Utils.startNewNode;
import static java.util.stream.IntStream.range;

public class ExampleAtomicOperations {

    public static void main(String[] args) {
        HazelcastInstance hz = startNewNode();
        String name = "node-" + hz.getCluster().getLocalMember().getAddress().getPort();
        Logger log = LoggerFactory.getLogger(name);
        IMap<String, Versioned<String>> test = hz.getMap("test");
        range(1, Integer.MAX_VALUE).forEach(counter -> {
            Versioned<String> expected = test.get("key");
            sleepUpTo(1000);
            test.compute("key", (key, actual) -> {
                if (actual == null) {
                    return new Versioned<>("hey-" + name + "-" + counter);
                }
                if (actual.getVersion() == expected.getVersion()) {
                    Versioned<String> updated = actual.increment().setValue("hey-" + name + "-" + counter);
                    log.info("Updated successfully: {}", updated);
                    return updated;

                } else {
                    log.warn("Optimistic error, expected version {}, but actual was {}",
                            expected.getVersion(), actual.getVersion());
                    return actual;
                }
            });
        });
        hz.shutdown();
    }

    public static class Versioned<T> implements Serializable {

        private final int version;
        private final T value;

        public Versioned(T value) {
            this.version = -1;
            this.value = value;
        }

        public Versioned(int version, T value) {
            this.version = version;
            this.value = value;
        }

        public int getVersion() {
            return this.version;
        }

        public Versioned<T> setVersion(int version) {
            return new Versioned<>(version, this.value);
        }

        public Versioned<T> increment() {
            return new Versioned<>(version + 1, this.value);
        }

        public T getValue() {
            return this.value;
        }

        public Versioned<T> setValue(T value) {
            return new Versioned<>(this.version, value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Versioned<?> versioned = (Versioned<?>) o;
            return version == versioned.version &&
                    Objects.equals(value, versioned.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(version, value);
        }

        @Override
        public String toString() {
            return "Versioned{" +
                    "version=" + version +
                    ", value=" + value +
                    '}';
        }
    }
}
