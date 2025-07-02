package com.icd;

import com.icd.ratelimiter.Algorithm;
import com.icd.ratelimiter.RateLimiter;
import com.icd.ratelimiter.rule.RateLimitRule;
import com.icd.ratelimiter.storage.RateLimiterStorage;

import java.util.Map;

public class RateLimiterFactory {

    private final Map<Algorithm, RateLimiterStorage> storageMap;

    public RateLimiterFactory(Map<Algorithm, RateLimiterStorage> storageMap) {
        this.storageMap = storageMap;
    }

    public RateLimiter create(Algorithm algorithm, RateLimitRule rule) {
        RateLimiterStorage storage = storageMap.get(algorithm);
        if (storage == null) {
            throw new IllegalArgumentException("No storage configured for algorithm: " + algorithm);
        }

        switch (algorithm) {
            case FIXED_WINDOW:
                return new FixedWindowRateLimiter(rule, storage);
            // case TOKEN_BUCKET:
            //    return new TokenBucketRateLimiter(rule, storage);
            default:
                throw new IllegalArgumentException("Unsupported algorithm: " + algorithm);
        }
    }
}
