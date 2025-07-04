package com.icd;

import com.icd.ratelimiter.RateLimiter;
import com.icd.ratelimiter.RequestContext;

import java.time.Instant;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/*
* 최근 N초(window) 동안 최대 x번(maxRequests) 까지 요청을 제한하는 모듈
* */
public class SlidingWindowRateLimiter implements RateLimiter {
    private final int maxRequests;
    private final long windowSizeMillis;
    private final Deque<Long> requestTimestamps;

    public SlidingWindowRateLimiter(int maxRequests, long windowSizeMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeMillis = windowSizeMillis;
        this.requestTimestamps = new ConcurrentLinkedDeque<>();
    }

    @Override
    public synchronized boolean allow(RequestContext context) {
        long now = Instant.now().toEpochMilli();

        // 현재 시간 기준으로 오래된 요청 삭제하기
        cleanupOldRequests(now);

        // 현재까지 들어온 요청 수가 maxRequests 보다 작을 떄 요청을 넣어준다
        if (requestTimestamps.size() < maxRequests) {
            requestTimestamps.addLast(now);
            return true;
        }
        return false;
    }

    // 현재시간 기준 오래된 요청 제거
    private void cleanupOldRequests(long now) {
        // 가장 오래된 요청 시간이 제한 시간을 넘겼을 경우
        while (!requestTimestamps.isEmpty() && now - requestTimestamps.peekFirst() > windowSizeMillis) {
            requestTimestamps.pollFirst(); // 가장 오래된 요청 제거
        }
    }

}
