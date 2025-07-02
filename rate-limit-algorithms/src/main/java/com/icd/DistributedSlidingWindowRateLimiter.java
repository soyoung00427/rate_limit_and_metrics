package com.icd;

import com.icd.ratelimiter.RateLimiter;
import com.icd.ratelimiter.RequestContext;
import com.icd.ratelimiter.storage.RateLimiterStorage;

import java.time.Duration;
import java.time.Instant;
/*
 * 분산서버 환경 Sliding Window 알고리즘
 */
public class DistributedSlidingWindowRateLimiter implements RateLimiter {

    private final int maxRequests;
    private final Duration windowSize;
    private final RateLimiterStorage storage;
    private final String ruleKey;  // 고유 키 구분용 (ex: "sliding_window")

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

        // 윈도우의 시작 시점(현재시간 - windowSize)
        Instant windowStart = now.minus(windowSize);

        // 저장소에서 요청 기록 증가 및 카운트 조회
        String composedKey = ruleKey + ":" + userKey;

        long count = storage.incrementAndGet(composedKey, windowStart, windowSize);

        return count <= maxRequests;
    }
}
