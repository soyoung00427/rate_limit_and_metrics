package com.icd;

import com.icd.ratelimiter.storage.RateLimiterStorage;

import java.time.Duration;
import java.time.Instant;

public class LeakyBucketInMemoryStorage implements RateLimiterStorage {

    @Override
    public long incrementAndGet(String key, Instant windowStart, Duration window) {
        return 0;
    }

    @Override
    public long getCount(String key, Instant windowStart) {
        return 0;
    }

    @Override
    public void cleanup() {

    }
}
