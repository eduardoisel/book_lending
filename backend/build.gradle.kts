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

    id("io.freefair.lombok") version "9.2.0"

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

    // https://www.geeksforgeeks.org/advance-java/using-lombok-to-reduce-boilerplate-code-in-spring-boot/ extra
    // From https://medium.com/@dulanjayasandaruwan1998/spring-doesnt-recommend-autowired-anymore-05fc05309dad
    // test replacing all autowired
    // Source: https://mvnrepository.com/artifact/org.projectlombok/lombok
    implementation("org.projectlombok:lombok")//:1.18.42

    implementation("org.springframework.boot:spring-boot-starter-web")

    //automatic documentation
    // https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.1")

    // https://mvnrepository.com/artifact/org.springframework.hateoas/spring-hateoas
    //implementation("org.springframework.hateoas:spring-hateoas")

    //org.gradle.kotlin.dsl.DependencyHandlerScope. // To get password encode //api("org.springframework.security:spring-security-core:6.3.2")

    // Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-actuator
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("org.springframework.boot:spring-boot-starter-security")


    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

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


    // https://mvnrepository.com/artifact/io.vavr/vavr
    implementation("io.vavr:vavr:0.11.0")
    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.13.2")


    // Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-data-jpa-test
    testImplementation("org.springframework.boot:spring-boot-data-jpa-test")

    // Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-cache-test
    implementation("org.springframework.boot:spring-boot-starter-cache-test")

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
