package com.icd.ratelimiter.http.filter;

import com.icd.ratelimiter.RateLimiter;
import com.icd.ratelimiter.RequestContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.time.Instant;
import java.util.Map;

/**
 * Feign 클라이언트에 레이트 리밋을 적용하는 인터셉터 구현체
 */
public class RateLimitFeignInterceptor implements RequestInterceptor {

    private final RateLimiter rateLimiter;

    public RateLimitFeignInterceptor(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Override
    public void apply(RequestTemplate template) {
        //메서드 + endpoint 조합으로 키 생성
        String key = template.method() + ":" + template.url();

        //RequestContext 생성
        RequestContext context = new RequestContext(key, Instant.now(), Map.of());

        //RateLimiter에 허용 여부 요청
        if (!rateLimiter.allow(context)) {
            throw new IllegalStateException("Rate limit exceeded for outbound Feign request: " + key);
        }
    }
}
