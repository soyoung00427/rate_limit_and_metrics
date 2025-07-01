package com.icd.ratelimiter.storage;

import java.time.Duration;
import java.time.Instant;

public interface RateLimiterStorage {
    long incrementAndGet(String key, Instant windowStart, Duration window);
    long getCount(String key, Instant windowStart);
    void cleanup();
}