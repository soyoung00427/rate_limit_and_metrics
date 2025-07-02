package com.icd;

import com.icd.ratelimiter.storage.RateLimiterStorage;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.time.Duration;
import java.time.Instant;

public class RedisSlidingWindowStorage implements RateLimiterStorage {

    private final JedisPool jedisPool;

    public RedisSlidingWindowStorage(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public long incrementAndGet(String key, Instant windowStart, Duration windowSize) {
        long now = Instant.now().toEpochMilli();
        long windowStartMillis = windowStart.toEpochMilli();
        long expireMillis = windowSize.toMillis();

        try (Jedis jedis = jedisPool.getResource()) {
            String script =
                    "local key = KEYS[1] " +
                    "local now = tonumber(ARGV[1]) " +
                    "local windowStart = tonumber(ARGV[2]) " +
                    "local expireMillis = tonumber(ARGV[3]) " +
                    "-- remove expired entries\n" +
                    "redis.call('ZREMRANGEBYSCORE', key, 0, windowStart) " +
                    "-- add current request\n" +
                    "redis.call('ZADD', key, now, now) " +
                    "-- expire key\n" +
                    "redis.call('PEXPIRE', key, expireMillis) " +
                    "-- count in window\n" +
                    "return redis.call('ZCOUNT', key, windowStart, now)";

            Object result = jedis.eval(script, 1,
                    key,
                    String.valueOf(now),
                    String.valueOf(windowStartMillis),
                    String.valueOf(expireMillis)
            );
            return (Long) result;
        }
    }

    @Override
    public long getCount(String key, Instant windowStart) {
        long now = Instant.now().toEpochMilli();
        long windowStartMillis = windowStart.toEpochMilli();

        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zcount(key, windowStartMillis, now);
        }
    }

    @Override
    public void cleanup() {
        // Redis는 TTL 기반 정리로 cleanup 불필요
    }
}
