package com.inc.ratelimiter;

public interface RateLimiter {
    boolean allow(RequestContext context);
}
