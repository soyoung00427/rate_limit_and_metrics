package com.inc.ratelimiter.condition;

import com.inc.ratelimiter.RequestContext;

import java.util.Objects;

/**
 * 특정 task의 Key 일치 여부 확인 조건
 */
public class TaskKeyCondition implements RateLimitCondition {
    private final String requiredTaskKey;

    public TaskKeyCondition(String requiredTaskKey) {
        this.requiredTaskKey = requiredTaskKey;
    }

    @Override
    public boolean matches(RequestContext context) {
        return Objects.equals(requiredTaskKey, context.getMetadata().get("taskKey"));
    }
}
