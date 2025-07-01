package com.icd.ratelimiter.storage;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory 기반 저장소 구현체
 */
public class CaffeinRateLimiterStorage implements RateLimiterStorage {
    private final ConcurrentMap<String, ValueWithExpire> store = new ConcurrentHashMap<>();

    @Override
    public long incrementAndGet(String key, Instant windowStart, Duration window) {
        String fullKey = key + ":" + windowStart.toString();
        ValueWithExpire value = store.compute(fullKey, (k, existing) -> {
            if (existing == null || existing.expireAt.isBefore(Instant.now())) {
                return new ValueWithExpire(new AtomicLong(1), Instant.now().plus(window));
            } else {
                existing.counter.incrementAndGet();
                return existing;
            }
        });
        return value.counter.get();
    }

    @Override
    public long getCount(String key, Instant windowStart) {
        String fullKey = key + ":" + windowStart.toString();
        ValueWithExpire value = store.get(fullKey);
        if (value == null || value.expireAt.isBefore(Instant.now())) {
            return 0;
        }
        return value.counter.get();
    }

    @Override
    public void cleanup() {
        Instant now = Instant.now();
        store.entrySet().removeIf(entry -> entry.getValue().expireAt.isBefore(now));
    }

    private static class ValueWithExpire {
        final AtomicLong counter;
        final Instant expireAt;

        ValueWithExpire(AtomicLong counter, Instant expireAt) {
            this.counter = counter;
            this.expireAt = expireAt;
        }
    }
}

