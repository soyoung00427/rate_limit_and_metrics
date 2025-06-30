package com.inc.ratelimiter.condition;

import com.inc.ratelimiter.RequestContext;

public interface RateLimitCondition {
    boolean matches(RequestContext context);
}
