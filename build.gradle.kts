import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	id("java")
	id("org.springframework.boot") version "2.7.18"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

val agent = configurations.create("agent")


java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

//configurations {
//	compileOnly {
//		extendsFrom annotationProcessor
//	}
//}

repositories {
	mavenCentral()
}

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
//    implementation("io.opentelemetry:opentelemetry-api")
// https://mvnrepository.com/artifact/io.opentelemetry/opentelemetry-api
	implementation("io.opentelemetry:opentelemetry-api:1.39.0")

    //spring modules
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

	compileOnly ("org.projectlombok:lombok:1.18.24")
	annotationProcessor ("org.projectlombok:lombok:1.18.24")

    agent("io.opentelemetry.javaagent:opentelemetry-javaagent:2.9.0")
	compileOnly ("org.projectlombok:lombok")
	annotationProcessor ("org.projectlombok:lombok")
	testImplementation ("org.springframework.boot:spring-boot-starter-test")
}

val copyAgent = tasks.register<Copy>("copyAgent") {
	from(agent.singleFile)
	into(layout.buildDirectory.dir("agent"))
	rename("opentelemetry-javaagent-.*\\.jar", "opentelemetry-javaagent.jar")
}

//val copyExtension = tasks.register<Copy>("copyExtension") {
//	from(extension.singleFile)
//	into(layout.buildDirectory.dir("agent"))
//	rename(".*\\.jar", "opentelemetry-javaagent-extension.jar")
//}

tasks.named<BootJar>("bootJar") {
	dependsOn(copyAgent)
//	dependsOn(copyExtension)

	archiveFileName = "app.jar"
}

