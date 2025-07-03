package com.icd;

import java.time.Instant;

public class LoggingRateLimitMetric implements RateLimitMetricListener {

    @Override
    public void onEvent(String policyKey, String userKey, boolean allowed, Instant timestamp) {
        String log = String.format(
            "[RateLimitMetric] ts=%s policy=%s user=%s allowed=%s",
            timestamp, policyKey, userKey, allowed
        );
        // 실서비스라면 logger.info(log)로!
        System.out.println(log);
    }
}
