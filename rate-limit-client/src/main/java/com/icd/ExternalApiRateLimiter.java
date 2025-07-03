package com.icd;

import com.icd.ratelimiter.RateLimiter;
import com.icd.ratelimiter.RequestContext;
import com.icd.ratelimiter.exception.RateLimitException;

import java.util.function.Supplier;

public class ExternalApiRateLimiter {
    private final RateLimiter rateLimiter;

    public ExternalApiRateLimiter(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    public <T> T callWithRateLimit(RequestContext context, Supplier<T> apiCall) {
        if (!rateLimiter.allow(context)) {
            throw new RateLimitException("Rate limit exceeded for key: " + context.getKey());
        }
        return apiCall.get();
    }
}
