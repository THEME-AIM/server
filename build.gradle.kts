import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
    kotlin("plugin.jpa") version "1.8.22"
    kotlin("kapt") version "1.7.10"
}

group = "com.aim"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}



allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

dependencies {
    // Kotlin Spring Libary
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // AIM Custom Libary
    // Security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // DB (JPA & QueryDSL)
    implementation("org.postgresql:postgresql")
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
    kapt("jakarta.annotation:jakarta.annotation-api")
    kapt("jakarta.persistence:jakarta.persistence-api")

    // Dev Tools
    developmentOnly("org.springframework.boot:spring-boot-devtools")


    // Utils
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
    implementation("com.google.code.gson:gson:2.10.1")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

    // Spring Rest Docs
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.restdocs:spring-restdocs-asciidoctor")
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

tasks.bootBuildImage {
    builder.set("paketobuildpacks/builder-jammy-base:latest")
}

// Rest Docs Setting

tasks {
    val snippetsDir = file("build/generated-snippets")

    test {
        outputs.dir(snippetsDir)
        useJUnitPlatform()
    }

    build {
        dependsOn("copyDocument")
    }

    asciidoctor {
        inputs.dir(snippetsDir)

        forkOptions {
            jvmArgs("--add-opens", "java.base/sun.nio.ch=ALL-UNNAMED", "--add-opens", "java.base/java.io=ALL-UNNAMED")
        }

        dependsOn(test)

        doFirst {
            delete("src/main/resources/static/docs")
        }
    }

    register<Copy>("copyDocument") {
        dependsOn(asciidoctor)

        destinationDir = file("src/main/resources/static")

        from("build/docs/asciidoc") {
            this.into("docs")
        }
    }

    bootJar {
        dependsOn(asciidoctor)

        from("build/docs/asciidoc") {
            into("BOOT-INF/classes/static/docs")
        }
    }
}