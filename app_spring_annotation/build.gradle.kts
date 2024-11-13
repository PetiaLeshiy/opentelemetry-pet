import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
	id("java")
	id("org.springframework.boot") version "3.2.2"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

val agent = configurations.create("agent")


java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
	implementation(platform("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom:2.9.0"))

	//spring modules
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter")


	compileOnly ("org.projectlombok:lombok:1.18.24")
	annotationProcessor ("org.projectlombok:lombok:1.18.24")

    agent("io.opentelemetry.javaagent:opentelemetry-javaagent:2.9.0")

	testImplementation ("org.springframework.boot:spring-boot-starter-test")
}

val copyAgent = tasks.register<Copy>("copyAgent") {
	from(agent.singleFile)
	into(layout.buildDirectory.dir("agent"))
	rename("opentelemetry-javaagent-.*\\.jar", "opentelemetry-javaagent.jar")
}

