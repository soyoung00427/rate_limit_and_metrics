package com.icd;

import java.time.Instant;

public interface RateLimitMetricListener {
    void onEvent(String policyKey, String userKey, boolean allowed, Instant timestamp);
}
