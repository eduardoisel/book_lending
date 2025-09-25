plugins {
    id("java")
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.6"

    checkstyle

    //not for this gradle, only groovy?
    //id ("nebula.lint") version "17.8.0"
}


group = "backend.bookSharing"
version = "1.0-SNAPSHOT"

/*
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
 */

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.eclipse.persistence:eclipselink:4.0.1")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // https://mvnrepository.com/artifact/org.springframework.hateoas/spring-hateoas
    implementation("org.springframework.hateoas:spring-hateoas:2.3.3")

    // To get password encode
    //api("org.springframework.security:spring-security-core:6.3.2")

    // https://mvnrepository.com/artifact/org.springframework.data/spring-data-jpa
    implementation("org.springframework.data:spring-data-jpa:4.0.0-M6")

    implementation("org.postgresql:postgresql:42.7.2")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
