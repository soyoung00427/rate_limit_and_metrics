package com.icd.ratelimiter.condition;

import com.icd.ratelimiter.RequestContext;

/**
 * 사용자 ID 기반 조건 구현체
 */
public class UserIdCondition implements RateLimitCondition {
    @Override
    public boolean matches(RequestContext context) {
        return context.getMetadata().containsKey("userId");
    }
}

