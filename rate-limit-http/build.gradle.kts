plugins {
    id("java")
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.icd"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2022.0.4")
    }
}

dependencies {
    implementation(project(":rate-limit-core"))
    implementation("javax.servlet:javax.servlet-api:4.0.1")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}