plugins {
    id("java")
    id("org.springframework.boot")
    id ("io.spring.dependency-management")
}

group = "com.icd"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation(project(":rate-limit-core"))
    implementation(project(":rate-limit-http"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2021.0.8")
    }
}

tasks.test {
    useJUnitPlatform()
}