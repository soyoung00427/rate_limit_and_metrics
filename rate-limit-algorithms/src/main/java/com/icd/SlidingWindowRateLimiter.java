package com.icd;

import com.icd.ratelimiter.RateLimiter;
import com.icd.ratelimiter.RequestContext;

import java.time.Instant;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;

/*
* 최근 N초(window) 동안 최대 x번(maxRequests) 까지 요청을 제한하는 모듈
* */
public class SlidingWindowRateLimiter implements RateLimiter {
    private final int maxRequests;
    private final long windowSizeMillis;
    private final ConcurrentMap<String, Deque<Long>> userRequestMap;
    //private final Deque<Long> requestTimestamps;

    public SlidingWindowRateLimiter(int maxRequests, long windowSizeMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeMillis = windowSizeMillis;
        this.userRequestMap = new ConcurrentHashMap<>();
       // this.requestTimestamps = new ConcurrentLinkedDeque<>();
    }

    @Override
    public boolean allow(RequestContext context) {
        String key = context.getKey();
        long now = getRequestTime(context);
        //
        Deque<Long> requestTimestamps = userRequestMap.computeIfAbsent(key, k -> new ConcurrentLinkedDeque<>());

        synchronized (requestTimestamps) {
            // 현재 시간 기준으로 오래된 요청 삭제하기
            cleanupOldRequests(requestTimestamps, now);

            // 현재까지 들어온 요청 수가 maxRequests 보다 작을 떄 요청을 넣어준다
            if (requestTimestamps.size() < maxRequests) {
                requestTimestamps.addLast(now);
                return true;
            }
            return false;
        }
    }

    /*
    * 요청 제한이 걸릴 떄, 다음 요청은 언제 가능한지 밀리초 단위로 알려주는 메소드
    * */
    public long getRetryAfterMillis(RequestContext context) {
        String key = context.getKey();
        long now = getRequestTime(context);
        Deque<Long> requestTimestamps = userRequestMap.computeIfAbsent(key, k -> new ConcurrentLinkedDeque<>());

        synchronized (requestTimestamps) {
            cleanupOldRequests(requestTimestamps, now);
            // 제한 횟수가 차지 않았다면 요청 가능함을 알려준다
            if(requestTimestamps.size() < maxRequests) {
                return 0L;
            } else {
                // ( 제일 처음 들어온 요청 시간 + 제한 시간 ) - 현재 시간
                Long oldest = requestTimestamps.peekFirst();
                if (oldest == null) {
                    return 0L; // 또는 적절한 fallback
                }
                return (oldest + windowSizeMillis) - now;
            }
        }
    }

    private long getRequestTime(RequestContext context) {
        return (context.getTimestamp() != null) ? context.getTimestamp().toEpochMilli() : Instant.now().toEpochMilli();
    }
    // 현재시간 기준 오래된 요청 제거
    private void cleanupOldRequests(Deque<Long> requestTimestamps, long now) {
        // 가장 오래된 요청 시간이 제한 시간을 넘겼을 경우
        while (!requestTimestamps.isEmpty() && now - requestTimestamps.peekFirst() > windowSizeMillis) {
            requestTimestamps.pollFirst(); // 가장 오래된 요청 제거
        }
    }

}
