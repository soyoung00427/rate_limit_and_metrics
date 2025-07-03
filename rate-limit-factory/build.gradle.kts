plugins {
    id("java")
}

group = "com.icd"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation(project(":rate-limit-core"))
    implementation(project(":rate-limit-algorithms"))
}

tasks.test {
    useJUnitPlatform()
}