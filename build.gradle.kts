plugins {
    java
    id ("org.springframework.boot") version "2.7.10" apply false
    id ("io.spring.dependency-management") version "1.1.7" apply false
}

group = "com.icd"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
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
}

tasks.withType<Test> {
    useJUnitPlatform()
}