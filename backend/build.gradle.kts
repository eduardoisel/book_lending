import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("java")
    id("org.springframework.boot") version "4.0.1"
    id("io.spring.dependency-management") version "1.1.7"

    // https://www.geeksforgeeks.org/advance-java/using-lombok-to-reduce-boilerplate-code-in-spring-boot/ extra
    // From https://medium.com/@dulanjayasandaruwan1998/spring-doesnt-recommend-autowired-anymore-05fc05309dad
    // Replaces all autowired by using RequiredArgsConstructor
    id("io.freefair.lombok") version "9.2.0"
    // 22/7
    // https://plugins.gradle.org/plugin/com.coditory.integration-test
    id("com.coditory.integration-test") version "2.2.5"

    //code formatter
    // https://plugins.gradle.org/plugin/com.diffplug.spotless
    id("com.diffplug.spotless") version "8.10.2"

}

// From https://github.com/diffplug/spotless/tree/main/plugin-gradle quickstart
spotless {

    format( "misc") {
    // define the files to apply `misc` to
    target ("*.gradle", ".gitattributes", ".gitignore")

    // define the steps to apply to those files
    trimTrailingWhitespace()
    leadingSpacesToTabs() // or leadingTabsToSpaces. Takes an integer argument if you don't like 4
    endWithNewline()
    }
    java {

        // apply a specific flavor of google-java-format
        googleJavaFormat("1.22.0").aosp().reflowLongStrings().skipJavadocFormatting()
        // fix formatting of type annotations
        formatAnnotations()
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(22)
    }
}

group = "backend.bookSharing"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

//https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Migration-Guide
dependencies {
    // automatic documentation (spring-docs)
    // https://springdoc.org/faq.html#_what_is_the_compatibility_matrix_of_springdoc_openapi_with_spring_boot
    // https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.1")

    // Source: https://mvnrepository.com/artifact/org.springframework/spring-orm
    implementation("org.springframework:spring-orm")

    // https://springdoc.org/#spring-security-support
    // Source: https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webflux-api
    implementation("org.springdoc:springdoc-openapi-starter-webflux-api:3.0.1")

    //below use javadoc (normal java documentation of classes and functions as documentation for swagger ui
    runtimeOnly("com.github.therapi:therapi-runtime-javadoc:0.15.0")
    annotationProcessor("com.github.therapi:therapi-runtime-javadoc-scribe:0.15.0")

    implementation("org.eclipse.persistence:eclipselink:4.0.1")

    // extra json parser, attempt to replace it with spring default
    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.13.2")


    //spring boot dependencies below

    implementation("org.springframework.boot:spring-boot-starter-web")

    // Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-actuator
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("org.springframework.boot:spring-boot-starter-security")

    //@WithMockUser https://docs.spring.io/spring-boot/how-to/testing.html
    testImplementation("org.springframework.boot:spring-boot-starter-security-test")


    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-cache
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.github.ben-manes.caffeine:caffeine")

    implementation("org.postgresql:postgresql:42.7.2")

    // Source: https://mvnrepository.com/artifact/org.hibernate.orm/hibernate-spatial
    implementation("org.hibernate.orm:hibernate-spatial:7.2.0.Final")

    // Source: https://mvnrepository.com/artifact/org.locationtech.jts/jts-core
    implementation("org.locationtech.jts:jts-core:1.20.0")

    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test
    testImplementation("org.springframework.boot:spring-boot-starter-test") //for @Autoconfigure mockMvc

    // Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-webmvc-test
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")

    // Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-data-jpa-test
    testImplementation("org.springframework.boot:spring-boot-data-jpa-test")

    // Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-cache-test
    testImplementation("org.springframework.boot:spring-boot-starter-cache-test")


    // Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-testcontainers
    integrationImplementation("org.springframework.boot:spring-boot-testcontainers")
    integrationImplementation("org.testcontainers:testcontainers-postgresql:2.0.3")
    // Source: https://mvnrepository.com/artifact/org.testcontainers/testcontainers-junit-jupiter
    integrationImplementation("org.testcontainers:testcontainers-junit-jupiter:2.0.3")
}

//https://docs.spring.io/spring-boot/how-to/build.html
springBoot {
    buildInfo()
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("failed")
    }
}

//from https://javadoc.io/doc/org.mockito/mockito-core/5.20.0/org.mockito/org/mockito/Mockito.html#0.3
//advised from when running test
//val mockitoAgent = configurations.create("mockitoAgent")
//dependencies{
//    testImplementation("org.mockito:mockito-core")
//    mockitoAgent("org.mockito:mockito-core") { isTransitive = false }
//}
//
//tasks.test{
//    jvmArgs!!.add("-javaagent:${mockitoAgent.asPath}")
//}


/*
 Addition of docker commands
 */
tasks.named<BootRun>("bootRun") {
    //should not be necessary with application.properties
    mainClass.set("backend.bookSharing.Main")
    dependsOn("dbAppUp")
    finalizedBy("dbAppDown")
}

tasks.register<Exec>("dbAppUp", fun Exec.() {
    commandLine("docker", "exec", "book-lending-container", "/app/bin/wait-for-postgres.sh", "localhost")

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

tasks.register<Exec>("dbAppDown", fun Exec.() {
    commandLine("docker", "compose", "-p", "book-lend", "-f", "./docker-compose.yml", "pause", "book-lending-app")
})
