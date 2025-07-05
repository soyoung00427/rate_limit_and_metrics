package com.icd;

import com.icd.ratelimiter.Algorithm;
import com.icd.ratelimiter.rule.RateLimitRule;
import com.icd.ratelimiter.storage.RateLimiterStorage;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

public class RateLimiterStorageProvider {

    private final Map<Algorithm, Function<RateLimitRule, RateLimiterStorage>> registry = new EnumMap<>(Algorithm.class);

    public RateLimiterStorageProvider() {
        registry.put(Algorithm.FIXED_WINDOW, rule -> new FixedWindowInMemoryStorage());
        registry.put(Algorithm.SLIDING_WINDOW, rule -> new SlidingWindowInMemoryStorage());
        registry.put(Algorithm.TOKEN_BUCKET, rule -> new TokenBucketInMemoryStorage());
        registry.put(Algorithm.LEAKY_BUCKET, rule -> new LeakyBucketInMemoryStorage());
    }

    public RateLimiterStorage getStorage(Algorithm algorithm, RateLimitRule rule) {
        if (!registry.containsKey(algorithm)) {
            throw new IllegalArgumentException("지원하지 않는 알고리즘입니다: " + algorithm);
        }
        return registry.get(algorithm).apply(rule);
    }
}

