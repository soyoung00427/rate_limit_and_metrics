package com.icd.ratelimiter;

public interface RateLimiter {
    boolean allow(RequestContext context);
}
