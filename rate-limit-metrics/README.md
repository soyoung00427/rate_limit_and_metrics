# LoggingRateLimitMetric

RateLimiter(요청 제한기)와 연동하여  
**요청 허용/차단 이벤트를 콘솔/파일/로그로 남기는**  
가장 간단한 메트릭(지표) 수집 Listener 입니다.

---

## 🚀 Quick Start

### 1. Listener(로깅 메트릭스) 생성

```java
RateLimitMetricListener metricListener = new LoggingRateLimitMetric();
```

---

### 2. RateLimiter에 MetricListener 연동

```java
// 생성자에 metricListener를 넘기거나, setter로 등록
int maxRequests = 100;
long windowSizeMillis = 60_000L; // 1분 = 60,000ms
String policyKey = "openai-api";

InMemorySlidingWindowRateLimiter limiter =
        new InMemorySlidingWindowRateLimiter(maxRequests, windowSizeMillis, policyKey);

```

---

### 3. 요청 허용/차단 시 로그 출력 확인

```java
RequestContext context = new RequestContext("user123", null);

boolean allowed = limiter.allow(context);
// => 자동으로 [RateLimit-Allow]/[RateLimit-Block] 로그가 출력됩니다.
```

---

## 📝 로그 예시

```
[RateLimit-Allow] ts=2025-07-04T10:01:10Z policy=ip user=user123 meta={}
[RateLimit-Block] ts=2025-07-04T10:01:11Z policy=ip user=user123 reason=RateLimitExceeded meta={}
```

---

## 🛠️ 특징

- 정책명, 사용자, 허용여부, 차단 사유, 추가정보(메타데이터) 모두 출력
- 기본은 System.out에 남기지만,  
  실제 서비스에서는 logger/slf4j로 파일 로그 등으로 쉽게 확장 가능
- DB, micrometer, 알림 등 다른 메트릭 시스템으로 Listener 교체도 매우 쉬움

---

## ⚙️ 확장/커스터마이징

- Logger(SLF4J 등)로 남기고 싶다면  
  `System.out.println` 대신 logger.info/warn 등으로 교체
- 여러 Listener(로깅+알림 등) 동시 사용하려면  
  Composite 패턴(`MultiRateLimitMetricListener`) 활용

---

## 📚 참고

- 다양한 메트릭 Listener(Logging, Micrometer, DB 등)는  
  metrics 패키지/문서 참고
- RateLimitMetricListener 인터페이스로 커스텀 후킹도 자유롭게 가능

---

## 🙌 Contributing

- 실사용 예시, 로그 포맷, 커스텀 확장 등 PR/이슈 언제든 환영!