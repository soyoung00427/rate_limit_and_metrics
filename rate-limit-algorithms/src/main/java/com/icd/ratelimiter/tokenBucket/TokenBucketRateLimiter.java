package com.icd.ratelimiter.tokenBucket;

import com.icd.ratelimiter.storage.RateLimiterStorage;

public class TokenBucketRateLimiter {

    private final long capacity; //버킷 최대 크기
    private final double tokenRate; //초당 몇 개의 토큰 충전할지
    private final RateLimiterStorage storage;

    public TokenBucketRateLimiter(long capacity, double tokenRate, RateLimiterStorage storage) {
        this.capacity = capacity;
        this.tokenRate = tokenRate;
        this.storage = storage;
    }

    public synchronized boolean tryAcquire(String token) {
        long now = System.currentTimeMillis();

        //1)현재 key에 대한 상태 조회
        TokenBucketState state = storage.get(key);
        if (state == null) {
            //상태가 없을 시 새로 초기화
            state = new TokenBucketState(capacity, now);
        }

        //2)토큰 충전
        long elapsedMillis = now - state.getLastRefillTimestamp();
        double tokensToAdd = (elapsedMillis / 1000.0) * refillRate;

        if (tokensToAdd > 0) {
            long newTokens = Math.min(capacity, state.getTokens() + (long) tokensToAdd);
            state.setTokens(newTokens);
            state.setLastRefillTimestamp(now);
        }

        //3)토큰 차감
        if (state.getTokens() > 0) {
            state.setTokens(state.getTokens() - 1);
            storage.save(key, state);
            return true;
        } else {
            return false;
        }
    }
}
