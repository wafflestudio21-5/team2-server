import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.1"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.21"
	kotlin("plugin.spring") version "1.9.21"
	kotlin("plugin.jpa") version "1.9.21"
}

group = "com.wafflestudio"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
	gradlePluginPortal()
}

dependencies {
	// language
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib")
	implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
	// spring
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	// json
	implementation("com.fasterxml.jackson.core:jackson-core:2.16.1")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	// db
	runtimeOnly("com.mysql:mysql-connector-j")
	// redis
	implementation("redis.clients:jedis:5.1.0")
	// jwt
	implementation("com.auth0:java-jwt:4.4.0")
	implementation("com.auth0:jwks-rsa:0.22.1")
	// logging
	implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
	//swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
	// aws
	implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")
	// websocket
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	// test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.getByName<Jar>("jar") {
	enabled = false
}
