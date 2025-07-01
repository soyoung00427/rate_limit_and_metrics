package com.icd.ratelimiter.condition;

import com.icd.ratelimiter.RequestContext;

public interface RateLimitCondition {
    boolean matches(RequestContext context);
}
