package com.icd.ratelimiter.api;

public interface RedisClient extends AutoCloseable {
    StatefulRedisConnection<String, String> connect();

    @Override
    void close();
}
