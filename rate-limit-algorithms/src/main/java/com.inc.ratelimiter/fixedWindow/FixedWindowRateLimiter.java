package com.inc.ratelimiter.fixedWindow;

import com.inc.ratelimiter.RateLimiter;
import com.inc.ratelimiter.storage.RateLimiterStorage;
import com.inc.ratelimiter.RequestContext;
import com.inc.ratelimiter.rule.RateLimitRule;

import java.time.Instant;

public class FixedWindowRateLimiter implements RateLimiter {

    private final RateLimitRule rule;
    private final RateLimiterStorage storage;

    public FixedWindowRateLimiter(RateLimitRule rule, RateLimiterStorage storage) {
        this.rule = rule;
        this.storage = storage;
    }

    @Override
    public boolean allow(RequestContext context) {
        // 조건 필터링
        if (!rule.getCondition().matches(context)) {
            return true; // 제한 대상 아님
        }

        // 현재 시간 기준 윈도우 시작 계산
        Instant windowStart = FixedWindowKeyGenerator.alignToWindow(context.getTimestamp(), rule.getWindow());

        // 고유 key 생성
        String composedKey = rule.getKey() + ":" + context.getKey() + ":" + windowStart.toString();

        // 카운트 증가 및 확인
        long count = storage.incrementAndGet(composedKey, windowStart, rule.getWindow());
        return count <= rule.getThreshold();
    }
}
