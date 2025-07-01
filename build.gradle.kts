plugins {
    java
    id ("org.springframework.boot") version "2.7.10" apply false
    id ("io.spring.dependency-management") version "1.1.7" apply false
}

group = "com.icd"
version = "0.0.1-SNAPSHOT"

subprojects {
    apply(plugin = "java")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(11))
        }
    }

    repositories {
        mavenCentral()
    }

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}