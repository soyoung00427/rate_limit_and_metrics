package com.icd;

import com.icd.ratelimiter.RateLimiter;
import com.icd.ratelimiter.RequestContext;

import java.time.Instant;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;

public class InMemorySlidingWindowRateLimiter implements RateLimiter {

    private final int maxRequests;
    private final long windowSizeMillis;
    private final String ruleKey; // 정책별 키(여러 정책 확장 고려)
    private final ConcurrentMap<String, Deque<Long>> userRequestMap = new ConcurrentHashMap<>();

    public InMemorySlidingWindowRateLimiter(int maxRequests, long windowSizeMillis, String ruleKey) {
        this.maxRequests = maxRequests;
        this.windowSizeMillis = windowSizeMillis;
        this.ruleKey = ruleKey;
    }

    @Override
    public boolean allow(RequestContext context) {
        String userKey = context.getKey();
        long now = context.getTimestamp() != null
                ? context.getTimestamp().toEpochMilli()
                : Instant.now().toEpochMilli();

        String composedKey = ruleKey + ":" + userKey;
        Deque<Long> timestamps = userRequestMap.computeIfAbsent(composedKey, k -> new ConcurrentLinkedDeque<>());

        synchronized (timestamps) {
            cleanupOldRequests(timestamps, now);
            if (timestamps.size() < maxRequests) {
                timestamps.addLast(now);
                return true;
            }
            return false;
        }
    }

    private void cleanupOldRequests(Deque<Long> timestamps, long now) {
        while (!timestamps.isEmpty() && now - timestamps.peekFirst() >= windowSizeMillis) {
            timestamps.pollFirst();
        }
    }

    // 필요시 retryAfter 반환 (RateLimit-Reset 응답 등에 활용)
    public long getRetryAfterMillis(RequestContext context) {
        String userKey = context.getKey();
        long now = context.getTimestamp() != null
                ? context.getTimestamp().toEpochMilli()
                : Instant.now().toEpochMilli();

        String composedKey = ruleKey + ":" + userKey;
        Deque<Long> timestamps = userRequestMap.computeIfAbsent(composedKey, k -> new ConcurrentLinkedDeque<>());

        synchronized (timestamps) {
            cleanupOldRequests(timestamps, now);
            if (timestamps.size() < maxRequests) return 0L;

            Long oldest = timestamps.peekFirst();
            if (oldest == null) return 0L;

            long retryAfter = (oldest + windowSizeMillis) - now;
            return retryAfter > 0 ? retryAfter : 0L;
        }
    }
}
