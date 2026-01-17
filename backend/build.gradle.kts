import java.util.LinkedList
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("java")
    id("org.springframework.boot") version "4.0.1"
    id("io.spring.dependency-management") version "1.1.7"

    //id("com.gorylenko.gradle-git-properties") version "2.5.4" // https://docs.spring.io/spring-boot/how-to/build.html#howto.build.generate-git-info
    checkstyle

    //not for this gradle, only groovy?
    //id ("nebula.lint") version "17.8.0"
    //kotlin("jvm")
}

//dependencyManagement {
//    imports {
//        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
//    }
//}

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
    // implementation("org.springframework.hateoas:spring-hateoas:3.0.1")

    //org.gradle.kotlin.dsl.DependencyHandlerScope. // To get password encode //api("org.springframework.security:spring-security-core:6.3.2")

    implementation("org.springframework.boot:spring-boot-starter-security")


    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("org.postgresql:postgresql:42.7.2")


    // Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-testcontainers
    testImplementation("org.springframework.boot:spring-boot-testcontainers")


    //testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test
    testImplementation("org.springframework.boot:spring-boot-starter-test")




    //annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")   https://docs.spring.io/spring-boot/specification/configuration-metadata/annotation-processor.html#appendix.configuration-metadata.annotation-processor


    // https://mvnrepository.com/artifact/io.vavr/vavr
    implementation("io.vavr:vavr:0.11.0")


    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.13.2")


    /* Do not exist for 3.x.x, use if upgrading to 4

    // Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-data-jpa-test
    testImplementation("org.springframework.boot:spring-boot-data-jpa-test")

    // Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-cache-test
    implementation("org.springframework.boot:spring-boot-starter-cache-test")
     */
}

//https://docs.spring.io/spring-boot/how-to/build.html
springBoot{
    buildInfo()
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
