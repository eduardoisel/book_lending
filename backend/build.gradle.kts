import java.util.LinkedList
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("java")
    id("org.springframework.boot") version "4.0.1"
    id("io.spring.dependency-management") version "1.1.7"

    //id("com.gorylenko.gradle-git-properties") version "2.5.4" // https://docs.spring.io/spring-boot/how-to/build.html#howto.build.generate-git-info
    checkstyle

    //not for this gradle, only groovy?
    //id ("nebula.lint") version "17.8.0" // https://www.javacodegeeks.com/intro-to-gradle-lint-plugin.html

    id("io.freefair.lombok") version "9.2.0"

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

//https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Migration-Guide
dependencies {
    // automatic documentation
    // https://springdoc.org/faq.html#_what_is_the_compatibility_matrix_of_springdoc_openapi_with_spring_boot
    // https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.1")

    // https://springdoc.org/#spring-security-support
    // Source: https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webflux-api
    implementation("org.springdoc:springdoc-openapi-starter-webflux-api:3.0.1")

    //below use javadoc (normal java documentation of classes and functions as documentation for swagger ui
    runtimeOnly("com.github.therapi:therapi-runtime-javadoc:0.15.0")
    java{
        annotationProcessor("com.github.therapi:therapi-runtime-javadoc-scribe:0.15.0")
    }

    implementation("org.eclipse.persistence:eclipselink:4.0.1")

    // https://www.geeksforgeeks.org/advance-java/using-lombok-to-reduce-boilerplate-code-in-spring-boot/ extra
    // From https://medium.com/@dulanjayasandaruwan1998/spring-doesnt-recommend-autowired-anymore-05fc05309dad
    // test replacing all autowired
    // Source: https://mvnrepository.com/artifact/org.projectlombok/lombok
    implementation("org.projectlombok:lombok")//:1.18.42

    implementation("org.springframework.boot:spring-boot-starter-web")

    // https://mvnrepository.com/artifact/org.springframework.hateoas/spring-hateoas
    //implementation("org.springframework.hateoas:spring-hateoas")

    // Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-actuator
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("org.springframework.boot:spring-boot-starter-security")

    //@WithMockUser https://docs.spring.io/spring-boot/how-to/testing.html //see
    testImplementation("org.springframework.boot:spring-boot-starter-security-test")


    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-cache
    implementation("org.springframework.boot:spring-boot-starter-cache")

    implementation("org.postgresql:postgresql:42.7.2")


    // Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-testcontainers
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:testcontainers-postgresql:2.0.1")
    // Source: https://mvnrepository.com/artifact/org.testcontainers/testcontainers-junit-jupiter
    testImplementation("org.testcontainers:testcontainers-junit-jupiter:2.0.3")


    testImplementation("io.rest-assured:rest-assured:6.0.0") // NEW, requires version specification?
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test
    testImplementation("org.springframework.boot:spring-boot-starter-test")


    //annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")   https://docs.spring.io/spring-boot/specification/configuration-metadata/annotation-processor.html#appendix.configuration-metadata.annotation-processor

    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.13.2")


    // Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-data-jpa-test
    testImplementation("org.springframework.boot:spring-boot-data-jpa-test")

    // Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-cache-test
    testImplementation("org.springframework.boot:spring-boot-starter-cache-test")

}

//https://docs.spring.io/spring-boot/how-to/build.html
springBoot{
    buildInfo()
}

tasks.test {
    useJUnitPlatform()
}


/*
 * https://github.com/mkyong/spring-boot/tree/master/spring-data-jpa-postgresql
 *
 * Project above uses postgres without sql file for initialization?
 *
 * https://docs.spring.io/spring-boot/how-to/data-initialization.html
 * Should be involved with this property
 */

tasks.named<BootRun>("bootRun") {
    //should not be necessary with application.properties
    mainClass.set("backend.bookSharing.Main")
    dependsOn("dbAppWait")
    finalizedBy("dbAppDown")
}

tasks.register<Exec>("dbAppUp", fun Exec.() {
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
})

tasks.register<Exec>("dbAppWait", fun Exec.() {
    commandLine("docker", "exec", "book-lending-container", "/app/bin/wait-for-postgres.sh", "localhost")

    dependsOn("dbAppUp")
})

tasks.register<Exec>("dbAppDown", fun Exec.() {
    commandLine("docker", "compose", "-p", "book-lend", "-f", "./docker-compose.yml", "pause", "book-lending-app")
})
