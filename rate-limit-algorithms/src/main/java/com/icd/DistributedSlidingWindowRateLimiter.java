package com.icd;

import com.icd.ratelimiter.RateLimiter;
import com.icd.ratelimiter.RequestContext;
import com.icd.ratelimiter.storage.RateLimiterStorage;

import java.time.Duration;
import java.time.Instant;
/*
분산 서버 환경에서 Sliding Window 방식으로 API 요청 한도를 관리하는 RateLimiter
 */
public class DistributedSlidingWindowRateLimiter implements RateLimiter {

    private final int maxRequests;
    private final Duration windowSize;
    private final RateLimiterStorage storage;
    private final String ruleKey;

    public DistributedSlidingWindowRateLimiter(int maxRequests, Duration windowSize, RateLimiterStorage storage, String ruleKey) {
        this.maxRequests = maxRequests;
        this.windowSize = windowSize;
        this.storage = storage;
        this.ruleKey = ruleKey;
    }

    @Override
    public boolean allow(RequestContext context) {
        String userKey = context.getKey();
        Instant now = context.getTimestamp() != null ? context.getTimestamp() : Instant.now();
        Instant windowStart = now.minus(windowSize);

        String composedKey = ruleKey + ":" + userKey;

        // storage 구현체가 windowStart~now 구간의 요청만 집계해야 함!
        long count = storage.incrementAndGet(composedKey, windowStart, windowSize);

        return count <= maxRequests;
    }
}
