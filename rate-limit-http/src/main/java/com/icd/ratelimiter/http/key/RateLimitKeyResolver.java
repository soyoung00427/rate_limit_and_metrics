package com.icd.ratelimiter.http.key;

import javax.servlet.http.HttpServletRequest;

/**
 * HTTP 요청으로부터 레이트 리밋 키를 추출하는 공통 인터페이스.
 */
public interface RateLimitKeyResolver {
    /**
     * 요청으로부터 식별 키를 생성합니다.
     * @param request 현재 HTTP 요청
     * @return 레이트 리미터 식별용 키
     */
    String resolveKey(HttpServletRequest request);
}
