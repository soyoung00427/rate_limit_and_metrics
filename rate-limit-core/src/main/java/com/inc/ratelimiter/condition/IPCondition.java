package com.inc.ratelimiter.condition;

import com.inc.ratelimiter.RequestContext;

/**
 * 기본 IP 조건 구현체
 */
public class IPCondition implements RateLimitCondition {
    @Override
    public boolean matches(RequestContext context) {
        return context.getMetadata().containsKey("ip");
    }
}
