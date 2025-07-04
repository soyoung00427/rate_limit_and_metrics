package com.icd;

import java.time.Instant;
import java.util.Map;

// 이벤트 후킹용 (허용/차단 분리, 사용자 정의 태그/메타데이터 포함)
public interface RateLimitMetricListener {
    void onAllow(String policyKey, String userKey, Instant timestamp, Map<String, Object> metadata);
    void onBlock(String policyKey, String userKey, Instant timestamp, Map<String, Object> metadata, String reason);
}
