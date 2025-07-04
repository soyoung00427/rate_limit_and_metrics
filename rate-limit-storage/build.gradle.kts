plugins {
    id("java")
}

group = "com.limit"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.lettuce:lettuce-core:6.7.1.RELEASE")

    implementation("com.google.code.gson:gson:2.13.1")

    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("ch.qos.logback:logback-classic:1.5.13")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("org.mockito:mockito-core:4.5.1")
    testImplementation("org.mockito:mockito-junit-jupiter:4.5.1")
    testImplementation("org.assertj:assertj-core:3.22.0")

    implementation(project(":rate-limit-core"))
}

tasks.test {
    useJUnitPlatform()
}