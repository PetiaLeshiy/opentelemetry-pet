plugins {
    id("java")

}

group = "org.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.auto.service:auto-service:1.1.1")
    implementation("io.opentelemetry.javaagent:opentelemetry-javaagent:2.9.0")
    implementation("io.opentelemetry:opentelemetry-sdk-trace:1.43.0")
    implementation("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure:1.43.0")
    implementation("io.opentelemetry:opentelemetry-semconv:1.29.0-alpha")
}
