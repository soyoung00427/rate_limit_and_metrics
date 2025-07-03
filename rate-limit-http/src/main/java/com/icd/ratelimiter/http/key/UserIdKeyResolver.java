package com.icd.ratelimiter.http.key;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class UserIdKeyResolver implements RateLimitKeyResolver {

    @Override
    public String resolveKey(HttpServletRequest request) {
        //1)"X-USER-ID" 헤더에서 사용자 ID 추출
        String userId = request.getHeader("X-USER-ID");
        if (userId != null && !userId.isEmpty()) {
            return userId;
        }

        //2)"user_id" 쿠키에서 사용자 ID 추출
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("user_id".equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().isEmpty()) {
                    return cookie.getValue();
                }
            }
        }

        // 3) 두 방법 모두 실패한 경우 예외 발생
        throw new IllegalArgumentException("요청에 X-USER-ID 헤더나 user_id 쿠키가 없습니다.");
    }
}
