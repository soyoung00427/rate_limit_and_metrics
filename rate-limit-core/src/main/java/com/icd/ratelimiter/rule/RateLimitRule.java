package com.icd.ratelimiter.rule;

import com.icd.ratelimiter.condition.RateLimitCondition;

import java.time.Duration;

/**
 * 하나의 limit 정책을 정의 하는 데이터 모델
 * new RateLimitRule("ip", new IPCondition(), 100, Duration.ofMinutes(1));
 */
public class RateLimitRule {

    /**
     * 요청 식별 키
     */
    private final String key;

    /**
     * RateLimitCondition 구현체
     */
    private final RateLimitCondition condition;

    /**
     * 주어진 시간(window)동안 허용할 최대 요청 수
     */
    private final int threshold;

    /**
     * 시간 범위 
     */
    private final Duration window;

    public RateLimitRule(String key, RateLimitCondition condition, int threshold, Duration window) {
        this.key = key;
        this.condition = condition;
        this.threshold = threshold;
        this.window = window;
    }

    public String getKey() {
        return key;
    }

    public RateLimitCondition getCondition() {
        return condition;
    }

    public int getThreshold() {
        return threshold;
    }

    public Duration getWindow() {
        return window;
    }
}