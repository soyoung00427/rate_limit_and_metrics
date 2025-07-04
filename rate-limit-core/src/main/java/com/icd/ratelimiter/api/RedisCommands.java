package com.icd.ratelimiter.api;

public interface RedisCommands<K, V> {
    String setex(K key, long seconds, V value);

    V get(K key);
}
