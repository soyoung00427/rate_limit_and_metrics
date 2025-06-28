plugins {
    java
    id ("org.springframework.boot") version "3.5.0"
    id ("io.spring.dependency-management") version "1.1.7"
}

group = "com.icd"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Core
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    // Redis
    implementation ("org.springframework.boot:spring-boot-starter-data-redis")

    // OpenTelemetry + Prometheus
    implementation ("io.opentelemetry:opentelemetry-api:1.34.0")
    implementation ("io.opentelemetry:opentelemetry-sdk:1.34.0")
    implementation("io.opentelemetry:opentelemetry-exporter-prometheus:1.39.0-alpha")

    // Configuration processor
    annotationProcessor ("org.springframework.boot:spring-boot-configuration-processor")
}

tasks.withType<Test> {
    useJUnitPlatform()
}