import java.util.LinkedList
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("java")
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.7"

    checkstyle

    //not for this gradle, only groovy?
    //id ("nebula.lint") version "17.8.0"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(22)
    }
}

group = "backend.bookSharing"
version = "1.0-SNAPSHOT"

val list: LinkedList<String> = LinkedList<String>()
list.add("src/main/resources") //is default

repositories {
    mavenCentral()
}

sourceSets {
    main {
        resources {
            setSrcDirs(list)
        }
    }
}

dependencies {
    implementation("org.eclipse.persistence:eclipselink:4.0.1")

    implementation("org.springframework.boot:spring-boot-starter-web")

    //automatic documentation
    // https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui
    //implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.13")

    // https://mvnrepository.com/artifact/org.springframework.hateoas/spring-hateoas
    // implementation("org.springframework.hateoas:spring-hateoas:2.3.3")

    // To get password encode
    //api("org.springframework.security:spring-security-core:6.3.2")

    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.5.6")

    implementation("org.postgresql:postgresql:42.7.2")

    /*
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    */

}

tasks.test {
    useJUnitPlatform()
}

tasks.named<BootRun>("bootRun") {
    //should not be necessary with application.properties
    environment("DB_URL", "jdbc:postgresql://localhost:5433/db?user=dbuser&password=changeit")
    dependsOn("dbAppWait")
    finalizedBy("dbAppDown")
}

task<Exec>("dbAppUp") {
    commandLine(
        "docker",
        "compose",
        "-p",
        "book-lend",
        "-f",
        "./docker-compose.yml",
        "up",
        "-d",
        "--build",
        "book-lending-app",
    )
}

task<Exec>("dbAppWait") {
    commandLine("docker", "exec", "book-lending-container", "/app/bin/wait-for-postgres.sh", "localhost")

    dependsOn("dbAppUp")
}

task<Exec>("dbAppDown") {
    commandLine("docker", "compose", "-p", "book-lend", "-f", "./docker-compose.yml", "pause", "book-lending-app")
}
