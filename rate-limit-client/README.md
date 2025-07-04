# ExternalApiRateLimiter

외부 API(Feign, RestTemplate, HttpClient 등) 호출 전에  
RateLimiter 정책(슬라이딩윈도우, 고정윈도우 등)을 손쉽게 적용할 수 있는 래퍼(데코레이터)입니다.

---

## 🚀 Quick Start

### 1. RateLimiter(정책) 인스턴스 생성

```java
// 예: 슬라이딩 윈도우 정책 (1분에 100회)
RateLimiter rateLimiter = new InMemorySlidingWindowRateLimiter(100, 60_000L, "openai-api");
```

---

### 2. ExternalApiRateLimiter 래퍼 생성

```java
ExternalApiRateLimiter apiLimiter = new ExternalApiRateLimiter(rateLimiter);
```

---

### 3. 외부 API 호출 감싸기

```java
RequestContext context = new RequestContext("user123", null);

String result = apiLimiter.callWithRateLimit(
    context,
    () -> openAiFeignClient.createChatCompletion(/* ... */)
);

// 또는 RestTemplate
ResponseEntity<KakaoResponse> response = apiLimiter.callWithRateLimit(
    context,
    () -> restTemplate.postForEntity("https://api.kakao.com/v2/send", body, KakaoResponse.class)
);
```

---

### 4. 한도 초과 시 예외 처리

```java
try {
    apiLimiter.callWithRateLimit(context, () -> someApiClient.someCall());
} catch (RateLimitException ex) {
    // 한도 초과! 사용자 안내, 로깅, 재시도 등 처리
}
```

---

## 🛠️ 특징

- 어떤 RateLimiter(슬라이딩윈도우, 고정윈도우, 토큰버킷 등)도 적용 가능
- 람다(Supplier)로 실제 외부 API 호출을 안전하게 감싸기만 하면 됨
- 한도 초과 시 예외 발생, 실제 HTTP 요청이 막힘

---

## 📦 다양한 정책 적용 예시

```java
ExternalApiRateLimiter slidingApiLimiter =
    new ExternalApiRateLimiter(new InMemorySlidingWindowRateLimiter(...));

ExternalApiRateLimiter fixedApiLimiter =
    new ExternalApiRateLimiter(new FixedWindowRateLimiter(...));

// 필요에 따라 여러 정책별로 인스턴스 구분 사용 가능
```

---

## 📚 참고

- 더 복잡한 정책, 여러 API 동시 관리, metrics 연동 등은 예제/문서 참고
- 정책 선택 팩토리 등 확장 구현도 가능

---

## 🙌 Contributing

- 사용성 개선, 예시 코드 추가 PR 환영!