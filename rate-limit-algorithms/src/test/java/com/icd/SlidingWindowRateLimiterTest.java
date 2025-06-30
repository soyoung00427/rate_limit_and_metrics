package com.icd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SlidingWindowRateLimiterTest {

    @Test
    public void testTryAcquire() throws InterruptedException {
        SlidingWindowRateLimiter limiter = new SlidingWindowRateLimiter(2, 1000);

        assertTrue(limiter.tryAcquire());
        assertFalse(limiter.tryAcquire());

        Thread.sleep(1100);

        assertTrue(limiter.tryAcquire());
    }
}
