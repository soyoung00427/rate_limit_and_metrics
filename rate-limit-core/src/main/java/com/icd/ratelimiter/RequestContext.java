package com.icd.ratelimiter;

import java.time.Instant;
import java.util.Map;

public class RequestContext {
    private final String key;
    private final Instant timestamp;
    private final Map<String, Object> metadata;

    public RequestContext(String key, Instant timestamp, Map<String, Object> metadata) {
        this.key = key;
        this.timestamp = timestamp;
        this.metadata = metadata;
    }

    public String getKey() {
        return key;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }
}
