package com.icd;

import com.icd.ratelimiter.Algorithm;
import com.icd.ratelimiter.RateLimiter;
import com.icd.ratelimiter.rule.RateLimitRule;
import com.icd.ratelimiter.storage.RateLimiterStorage;

public class RateLimiterFactory {

    private final RateLimiterStorage storage;

    public RateLimiterFactory(RateLimiterStorage storage) {
        this.storage = storage;
    }

    public RateLimiter create(Algorithm algorithm, RateLimitRule rule) {
        switch (algorithm) {
            case FIXED_WINDOW:
                return new FixedWindowRateLimiter(rule, storage);
            // case SLIDING_WINDOW:
            //     return new SlidingWindowRateLimiter(rule, storage);
            // case TOKEN_BUCKET:
            //     return new TokenBucketRateLimiter(rule, storage);
            // case LEAKY_BUCKET;
            //     return new LeakyBucketRateLimiter(rule, storage);
            default:
                throw new IllegalArgumentException("Unsupported algorithm: " + algorithm);
        }
    }
}
