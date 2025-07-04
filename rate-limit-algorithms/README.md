# InMemorySlidingWindowRateLimiter

단일 서버(싱글 인스턴스) 환경에서  
**Sliding Window 알고리즘**으로  
API/서비스 요청 한도를 메모리 기반으로 효율적으로 관리하는 RateLimiter 구현체입니다.

---

## 🚀 Quick Start

```java
int maxRequests = 100;
long windowSizeMillis = 60_000L; // 1분
String policyKey = "openai-api";

InMemorySlidingWindowRateLimiter limiter =
    new InMemorySlidingWindowRateLimiter(maxRequests, windowSizeMillis, policyKey);

RequestContext context = new RequestContext("user123", null);
/*
if (limiter.allow(context)) {
        // 요청 허용됨: 외부 API 호출 등 비즈니스 로직 실행
        System.out.println("API call allowed");
} else {
        // 요청 차단: 한도 초과
        System.err.println("Rate limit exceeded!");
}
*/

```

---


---

# DistributedSlidingWindowRateLimiter

여러 서버(분산 환경)에서  
**Sliding Window 알고리즘**으로  
API/서비스 요청 한도를 통합·일관되게 관리할 수 있는  
외부 스토리지(Redis, DB 등) 기반 RateLimiter 구현체입니다.

---

## 🚀 Quick Start

```java
/*
int maxRequests = 100;
Duration windowSize = Duration.ofMinutes(1);
RateLimiterStorage storage = ...; // Redis 등 외부 스토리지 구현체
String policyKey = "openai-api";

DistributedSlidingWindowRateLimiter limiter =
    new DistributedSlidingWindowRateLimiter(maxRequests, windowSize, storage, policyKey);

RequestContext context = new RequestContext("user123", null);

if (limiter.allow(context)) {
    // 요청 허용됨: 외부 API 호출 등 비즈니스 로직 실행
} else {
    // 요청 차단: 한도 초과
}

 */
```
