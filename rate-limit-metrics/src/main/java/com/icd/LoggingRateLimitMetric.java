package com.icd;

import java.time.Instant;
import java.util.Map;

public class LoggingRateLimitMetric implements RateLimitMetricListener {

    @Override
    public void onAllow(String policyKey, String userKey, Instant timestamp, Map<String, Object> metadata) {
        System.out.printf("[RateLimit-Allow] ts=%s policy=%s user=%s meta=%s\n",
                timestamp, policyKey, userKey, metadata);
    }

    @Override
    public void onBlock(String policyKey, String userKey, Instant timestamp, Map<String, Object> metadata, String reason) {
        System.out.printf("[RateLimit-Block] ts=%s policy=%s user=%s reason=%s meta=%s\n",
                timestamp, policyKey, userKey, reason, metadata);
    }
}
