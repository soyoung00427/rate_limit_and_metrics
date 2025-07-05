package com.icd.ratelimiter.http.filter;

import com.icd.ratelimiter.RateLimiter;
import com.icd.ratelimiter.RequestContext;
import com.icd.ratelimiter.http.key.RateLimitKeyResolver;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 서블릿 환경에서 HTTP 요청에 레이트 리밋을 적용하는 전역 필터 구현체
 */
public class RateLimitServletFilter implements Filter {

    private final RateLimiter rateLimiter;
    private final RateLimitKeyResolver keyResolver;

    /**
     * RateLimiter와 KeyResolver를 생성자 주입합니다.
     *
     * @param rateLimiter 레이트 리미터 알고리즘
     * @param keyResolver 요청에서 식별 키를 생성하는 리졸버
     */
    public RateLimitServletFilter(RateLimiter rateLimiter, RateLimitKeyResolver keyResolver) {
        this.rateLimiter = rateLimiter;
        this.keyResolver = keyResolver;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        try {
            //key 추출
            String key = keyResolver.resolveKey(request);

            //RequestContext를 생성
            RequestContext context = new RequestContext(key, Map.of());

            //RateLimiter에 허용 여부 요청
            if (rateLimiter.allow(context)) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                //거절된 요청
                response.setStatus(429);
                response.getWriter().write("Too Many Requests");
                return;
            }
        } catch (Exception e) {
            //기타 예외
            response.setStatus(400);
            response.getWriter().write("Invalid Request: " + e.getMessage());
        }
    }

}
