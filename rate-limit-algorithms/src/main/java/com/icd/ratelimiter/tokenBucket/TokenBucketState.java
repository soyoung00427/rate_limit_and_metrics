package com.icd.ratelimiter.tokenBucket;

public class TokenBucketState {
    private long tokens; //현재 남은 토큰 수
    private long lastRefillTimestamp; //마지막 토큰 충전 시각

    public TokenBucketState(long tokens, long lastRefillTimestamp) {
        this.tokens = tokens;
        this.lastRefillTimestamp = lastRefillTimestamp;
    }

    public long getTokens() {
        return tokens;
    }

    public void setTokens(long tokens) {
        this.tokens = tokens;
    }

    public long getLastRefillTimestamp() {
        return lastRefillTimestamp;
    }

    public void setLastRefillTimestamp(long lastRefillTimestamp) {
        this.lastRefillTimestamp = lastRefillTimestamp;
    }
}
