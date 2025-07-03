# Rate Limiter

---
Rate Limiter는 Java 기반 애플리케이션을 위한 확장 가능한 처리율 제한 라이브러리입니다. 본 라이브러리는 시스템 안정성과 성능 보장을 위해 요청 처리율을 정밀하게 제어합니다. 본 라이브러리는 API 남용 방지, 서버 부하 분산, 자원 효율적 관리라는 핵심 목표를 달성하기 위해 다양한 알고리즘, 유연한 스토리지 솔루션, 그리고 Spring 및 HTTP 클라이언트 연동 기능을 제공함으로써 개발자가 레이트 리미팅을 효과적으로 구현하고 관리할 수 있도록 지원합니다.

---
## ✨ 주요 기능

### 1. 다양한 알고리즘 지원
고정 윈도우, 슬라이딩 윈도우, 토큰 버킷, 리키 버킷 등 업계 표준 레이트 리미팅 알고리즘을 구현하여 제공합니다.

### 2. 유연한 스토리지
인메모리 스토리지(InMemoryRateLimitStorage)를 기본 제공하여 신속한 개발 환경 구축 및 단일 인스턴스 애플리케이션에 적합합니다. 향후 분산 환경을 위한 Redis 스토리지(RedisRateLimitStorage)를 지원하여 서버 인스턴스 간 일관된 제한 정책 유지를 가능하게 할 예정입니다. 커스텀 스토리지 구현 또한 가능합니다.

### 3. 간편한 통합
Spring WebMVC (ServletFilter), Spring WebFlux (WebFluxFilter), OpenFeign 클라이언트 (RateLimitFeignInterceptor) 등 다양한 HTTP 요청 처리 프레임워크와의 원활한 통합을 지원합니다. RateLimitKeyResolver는 HTTP 요청으로부터 고유 키를 자동 추출하여 통합 편의성을 높입니다.

### 4. Spring Boot 호환성
Spring Boot에 최적화된 모듈(rate-limiter-spring-starter)은 @RateLimit 어노테이션과 Spring AOP를 활용하여 메서드 단위 레이트 리미팅을 선언적으로 적용합니다. 이는 비즈니스 로직과 레이트 리미팅 로직을 분리하여 코드 가독성 및 유지보수성을 향상시킵니다.

### 5. 메트릭 및 로깅
Micrometer를 통한 상세 메트릭 수집 및 이벤트 로깅 기능을 제공하여 시스템 가시성을 높이고 효율적인 모니터링을 지원합니다. Prometheus와 같은 모니터링 시스템과의 연동 및 상세 로깅은 디버깅과 문제 해결에 필수적입니다.

### 6. 확장성
RateLimiterStorage 인터페이스 및 MetricsCollector 추상화와 같은 유연한 확장 포인트를 제공함으로써, 사용자는 커스텀 스토리지 구현 또는 기존 메트릭 시스템과의 원활한 연동을 실현할 수 있습니다.

---

## 📦 모듈 구성
rate-limiter 프로젝트는 다음과 같은 모듈로 구성되어 있으며, 각 모듈은 독립적인 기능을 수행함과 동시에 유기적으로 결합되어 포괄적인 레이트 리미팅 솔루션을 제공합니다.

```text
1. rate-limit-core
2. rate-limit-storage
3. rate-limit-algorithm
4. rate-limit-factory
5. rate-limit-http
6. rate-limit-metrics
7. rate-limit-client
8. rate-limit-spring-starter
9. rate-limit-sample-app
```


### 1. rate-limiter-core

본 모듈은 레이트 리미터의 핵심 정의와 공통 인터페이스를 포괄하며, 라이브러리의 모든 레이트 리미팅 기능에 대한 근간을 제공합니다.


### 2. rate-limiter-storage

레이트 리미팅 데이터를 저장하고 관리하기 위한 추상화 및 인메모리 구현체를 제공합니다.

### 3. rate-limiter-algorithm

널리 사용되는 다양한 레이트 리미팅 알고리즘의 구현체를 포함하며, 각 알고리즘은 RateLimiter 인터페이스를 구현합니다.

### 4. rate-limiter-factory

다양한 Rate Limiting 알고리즘과 저장소 구현체들을 조합하여
간편하게 `RateLimiter` 인스턴스를 생성할 수 있도록 도와주는 진입점(Entry Point) 모듈입니다.

이 모듈을 통해 개발자는 복잡한 설정 없이도 필요한 알고리즘과 저장소를 연결해
유연하고 확장 가능한 Rate Limiting 기능을 바로 사용할 수 있습니다.

### 5. rate-limiter-http

HTTP 요청 환경과의 연동을 위한 컴포넌트를 제공하여, 웹 애플리케이션에 레이트 리미팅을 손쉽게 적용할 수 있도록 합니다.

### 6. rate-limiter-metrics

레이트 리미팅 활동에 대한 로깅 및 메트릭 기능을 제공하여 시스템의 가시성을 높이고 효율적인 모니터링을 지원합니다.

### 7. rate-limiter-client

외부 API 호출 시 레이트 리미팅을 적용하기 위한 래퍼 모듈입니다.

### 8. rate-limiter-spring-starter

Spring 애플리케이션에서 어노테이션과 Spring AOP를 통해 레이트 리미팅을 선언적으로 적용할 수 있도록 돕는 모듈입니다.

### 9. rate-limiter-sample-app

본 라이브러리의 실제 사용 예시 및 데모를 포함하는 프로젝트로, 향후 확장될 예정입니다.

---
## 🚀 시작하기
rate-limiter를 프로젝트에 통합하고 활용하는 절차는 다음과 같습니다.

### 1. 의존성 추가
Maven 또는 Gradle과 같은 빌드 도구를 사용하여 필요한 모듈을 프로젝트의 의존성으로 추가합니다. 다음은 예시 코드입니다.

```xml
<!-- Maven -->
<dependencies>
    <dependency>
        <groupId>com.innerCircle.ratelimiter</groupId>
        <artifactId>rate-limiter-spring-starter</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>com.innerCircle.ratelimiter</groupId>
        <artifactId>rate-limiter-algorithm</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

```gradle
// Gradle
implementation 'com.innerCircle.ratelimiter:rate-limiter-spring-starter:1.0.0-SNAPSHOT'
implementation 'com.innerCircle.ratelimiter:rate-limiter-algorithm:1.0.0-SNAPSHOT'
```

2. ### `@RateLimit` 어노테이션 활용 예시

Spring Boot 애플리케이션 내에서 `@RateLimit` 어노테이션을 사용하여 특정 메서드에 레이트 리미팅 정책을 선언적으로 적용할 수 있습니다.

```java

```

---
## 🔌 스토리지 확장 (향후 계획)

현재는 `InMemoryRateLimitStorage`를 제공하고 있으나, 향후 다양한 스토리지 옵션을 추가로 지원할 계획입니다.

* **`RedisRateLimitStorage`**: (계획 중) Redis 기반의 분산 환경용 스토리지 구현체로, 다수의 애플리케이션 인스턴스 간 일관된 제한을 보장합니다.

* **`CustomStorage` 템플릿**: (계획 중) 사용자가 특정 인프라에 맞는 스토리지 솔루션을 쉽게 구현할 수 있도록 가이드라인과 템플릿을 제공할 예정입니다.

---
## 🤝 기여 안내

본 프로젝트에 대한 기여는 언제든지 환영합니다. 이슈 보고, 기능 제안, 코드 기여 등 다양한 방식으로 참여하실 수 있습니다.

---
## 📜 라이선스 정보

본 프로젝트는 MIT 라이선스에 의거하여 배포됩니다. 자세한 내용은 `LICENSE` 파일을 통해 확인하실 수 있습니다.

---
## 📞 문의처

추가적인 문의 사항이나 개선 제안이 있으실 경우, GitHub Issues를 통해 문의하여 주시기 바랍니다.
