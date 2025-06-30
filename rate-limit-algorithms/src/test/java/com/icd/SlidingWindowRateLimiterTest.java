package com.icd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SlidingWindowRateLimiterTest {

    @Test
    void shouldAllowRequestsWithinLimit() {
        SlidingWindowRateLimiter limiter = new SlidingWindowRateLimiter(5, 1000); // 1초에 5번

        for(int i = 0; i < 5; i++) {
            assertTrue(limiter.tryAcquire(), "요청" + (i+1) + " 은 허용돼야 함");
        }

    }

    @Test
    void shouldNotAllowRequestsWithoutLimit() {
        SlidingWindowRateLimiter limiter = new SlidingWindowRateLimiter(3, 1000);
        assertTrue(limiter.tryAcquire());
        assertTrue(limiter.tryAcquire());
        assertTrue(limiter.tryAcquire());

        assertFalse(limiter.tryAcquire(), "4째 요청은 거절돼야함");
    }

    @Test
    void shouldAllowNewRequestsAfterOldOnesExpire() throws InterruptedException {
        SlidingWindowRateLimiter limiter = new SlidingWindowRateLimiter(2, 500);
        assertTrue(limiter.tryAcquire());
        assertTrue(limiter.tryAcquire());

        assertFalse(limiter.tryAcquire());
        Thread.sleep(600);

        assertTrue(limiter.tryAcquire());

    }
}
