package com.icd.ratelimiter.http.key;

import javax.servlet.http.HttpServletRequest;

public class IpKeyResolver implements RateLimitKeyResolver {

    @Override
    public String resolveKey(HttpServletRequest request) {
        //1)클라이언트가 프록시/로드밸런서를 거친 경우,
        //X-Forwarded-For 헤더에 담겨 있는 실제 클라이언트 IP 추출
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isEmpty()) {
            //X-Forwarded-For가 여러 IP로 구성될 수 있으니 첫 번째 IP를 사용
            return forwardedFor.split(",")[0].trim();
        }
        //2)X-Forwarded-For가 없는 경우,
        //getRemoteAddr()로 요청을 보낸 클라이언트의 IP를 문자열로 반환
        return request.getRemoteAddr();
    }
}
