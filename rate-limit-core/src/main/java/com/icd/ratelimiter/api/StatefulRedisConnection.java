package com.icd.ratelimiter.api;

public interface StatefulRedisConnection<K, V> extends AutoCloseable {
    RedisCommands<K, V> sync();

    boolean isOpen();

    @Override
    void close();
}
