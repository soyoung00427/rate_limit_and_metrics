package com.icd;

import com.icd.ratelimiter.RateLimiter;
import com.icd.ratelimiter.RequestContext;

import java.time.Instant;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;
/*
* 단일서버 환경 Sliding Window 알고리즘
*/
public class InMemorySlidingWindowRateLimiter implements RateLimiter {

    private final int maxRequests;
    private final long windowSizeMillis;
    private final ConcurrentMap<String, Deque<Long>> userRequestMap = new ConcurrentHashMap<>();

    public InMemorySlidingWindowRateLimiter(int maxRequests, long windowSizeMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeMillis = windowSizeMillis;
    }

    @Override
    public boolean allow(RequestContext context) {
        String key = context.getKey();
        long now = Instant.now().toEpochMilli();

        Deque<Long> timestamps = userRequestMap.computeIfAbsent(key, k -> new ConcurrentLinkedDeque<>());

        synchronized (timestamps) {
            cleanupOldRequests(timestamps, now);
            if (timestamps.size() < maxRequests) {
                timestamps.addLast(now);
                return true;
            }
            return false;
        }
    }

    public long getRetryAfterMillis(RequestContext context) {
        String key = context.getKey();
        long now = Instant.now().toEpochMilli();

        Deque<Long> timestamps = userRequestMap.computeIfAbsent(key, k -> new ConcurrentLinkedDeque<>());

        synchronized (timestamps) {
            cleanupOldRequests(timestamps, now);

            if (timestamps.size() < maxRequests) {
                return 0L;
            }

            Long oldest = timestamps.peekFirst();
            if (oldest == null) {
                return 0L;
            }

            long retryAfter = (oldest + windowSizeMillis) - now;
            return retryAfter > 0 ? retryAfter : 0L;
        }
    }

    private void cleanupOldRequests(Deque<Long> timestamps, long now) {
        while (!timestamps.isEmpty() && now - timestamps.peekFirst() > windowSizeMillis) {
            timestamps.pollFirst();
        }
    }
}
