package com.icd.ratelimiter.condition;

/**
 * Condition을 AND로 묶는 복합 조건
 */
import com.icd.ratelimiter.RequestContext;

import java.util.List;

public class AndCondition implements RateLimitCondition {
    private final List<RateLimitCondition> conditions;

    public AndCondition(List<RateLimitCondition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean matches(RequestContext context) {
        return conditions.stream().allMatch(cond -> cond.matches(context));
    }
}
