package com.icd.ratelimiter.http.key;

import javax.servlet.http.HttpServletRequest;

public class TaskKeyResolver implements RateLimitKeyResolver {

    @Override
    public String resolveKey(HttpServletRequest request) {
        //요청 파라미터 "taskKey"로부터 태스크 키 추출
        String taskKey = request.getParameter("taskKey");
        if (taskKey == null || taskKey.isEmpty()) {
            throw new IllegalArgumentException("요청에 taskKey가 없습니다.");
        }
        return taskKey;
    }
}
