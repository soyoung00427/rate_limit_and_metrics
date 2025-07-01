package com.icd;

import com.icd.ratelimiter.RequestContext;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class SlidingWindowRateLimiterTest {

    @Test
    void shouldAllowRequestsWithinLimit() {
        SlidingWindowRateLimiter limiter = new SlidingWindowRateLimiter(5, 1000); // 1초에 5번

        for(int i = 0; i < 5; i++) {
            assertTrue(limiter.allow(new RequestContext("key", Instant.now(), Collections.emptyMap())), "요청" + (i+1) + " 은 허용돼야 함");
        }

    }

    @Test
    void shouldNotAllowRequestsWithoutLimit() {
        SlidingWindowRateLimiter limiter = new SlidingWindowRateLimiter(3, 1000);
        assertTrue(limiter.allow(new RequestContext("key", Instant.now(), Collections.emptyMap())));
        assertTrue(limiter.allow(new RequestContext("key", Instant.now(), Collections.emptyMap())));
        assertTrue(limiter.allow(new RequestContext("key", Instant.now(), Collections.emptyMap())));

        assertFalse(limiter.allow(new RequestContext("key", Instant.now(), Collections.emptyMap())), "4째 요청은 거절돼야함");
    }

    @Test
    void shouldAllowNewRequestsAfterOldOnesExpire() throws InterruptedException {
        SlidingWindowRateLimiter limiter = new SlidingWindowRateLimiter(2, 500);
        assertTrue(limiter.allow(new RequestContext("key", Instant.now(), Collections.emptyMap())));
        assertTrue(limiter.allow(new RequestContext("key", Instant.now(), Collections.emptyMap())));

        assertFalse(limiter.allow(new RequestContext("key", Instant.now(), Collections.emptyMap())));
        Thread.sleep(600);

        assertTrue(limiter.allow(new RequestContext("key", Instant.now(), Collections.emptyMap())));

    }

    @Test
    void rateLimitWithMultipleThreads() throws InterruptedException {
        int maxRequests = 5;
        long windowSizeInMillis = 1000;
        SlidingWindowRateLimiter rateLimiter = new SlidingWindowRateLimiter(maxRequests, windowSizeInMillis);

        int threadCount = 10;
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger allowedCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                boolean allowed = rateLimiter.allow(new RequestContext("key", Instant.now(), Collections.emptyMap()));
                if (allowed) {
                    allowedCount.incrementAndGet();
                }
                latch.countDown();
            }).start();
        }

        latch.await();

        System.out.println("허용된 요청 수: " + allowedCount.get());

        // 최대 요청 수를 넘지 않아야 함
        assertTrue(allowedCount.get() <= maxRequests);
    }

    @Test
    void testConcurrentAccessAndQps() throws InterruptedException {
        int maxRequests = 100;
        long windowSizeMillis = 1000; // 1초
        SlidingWindowRateLimiter rateLimiter = new SlidingWindowRateLimiter(maxRequests, windowSizeMillis);

        int totalThreads = 500; // 총 요청 수
        AtomicInteger allowedCount = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(totalThreads);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < totalThreads; i++) {
            new Thread(() -> {
                if (rateLimiter.allow(new RequestContext("key", Instant.now(), Collections.emptyMap()))) {
                    allowedCount.incrementAndGet();
                }
                latch.countDown();
            }).start();
        }

        latch.await(); // 모든 스레드 완료 대기
        long endTime = System.currentTimeMillis();

        double durationSeconds = (endTime - startTime) / 1000.0;
        double qps = totalThreads / durationSeconds;

        System.out.println("✅ 허용된 요청 수: " + allowedCount.get());
        System.out.println("⏱ 실행 시간: " + durationSeconds + "초");
        System.out.println("📊 QPS: " + qps);

        // 허용된 요청 수는 maxRequests 이하여야 한다
        assertTrue(allowedCount.get() <= maxRequests);
    }
}
