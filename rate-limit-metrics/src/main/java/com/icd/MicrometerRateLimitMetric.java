package com.icd;

import io.micrometer.core.instrument.MeterRegistry;
import java.time.Instant;
import java.util.Map;
/*
* RateLimiter의 허용/차단 이벤트를 Micrometer 메트릭(카운터 등)으로 수집해서,
* Prometheus, Grafana 등 외부 모니터링 시스템에 자동 집계해주는 리스너
*/
public class MicrometerRateLimitMetric implements RateLimitMetricListener {

    private final MeterRegistry registry;

    public MicrometerRateLimitMetric(MeterRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void onAllow(String policyKey, String userKey, Instant timestamp, Map<String, Object> metadata) {
        registry.counter("ratelimit.allow",
                "policy", policyKey,
                "user", userKey
        ).increment();
    }

    @Override
    public void onBlock(String policyKey, String userKey, Instant timestamp, Map<String, Object> metadata, String reason) {
        registry.counter("ratelimit.block",
                "policy", policyKey,
                "user", userKey,
                "reason", reason
        ).increment();
    }
}
