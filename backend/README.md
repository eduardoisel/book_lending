# About this project

This project is for personal training of server backend technologies, focusing on using a database with 
JPA with spring tools as much as possible.

I have done my best to comment my code, so I believe it is a good example to see what spring can automate for other people
starting out. That being said, I have not explained basic concepts such as dependency injection. If you do not understand
why created classes are never instantiated explicitly on code, you should not look to this project yet. 
[I advise reading the official documentation](https://docs.spring.io/spring-framework/reference/core.html).

## TODO

Admin user capabilities may also be added, allowing special operations like suspending/banning users, such as 
harassment through any communication in the server or adding fake books. The damaging books or failing to return them
to their owners after the time limit will also be added, even if in this person to person lending framework may not 
allow any realistic way for these cases to be actually verified.

Testing framework is still being developed, as there are kinks with test containers to be solved.

Http capabilities are underdeveloped. Looking into adding cache and possibly using hateoas.

A real implementation of this service would also use better tools to judge the proximity of users, as this is a service
about physically lending books. Unless it just saves user input coordinates and judge distance by ignoring actual
terrain such as oceans, a significant external service would be needed.

Smaller things to solve on current code may be marked with todo text. 

## Conversion to maven

If you do not like gradle you will need to

* Change the dependencies, see [here](https://docs.gradle.org/current/userguide/migrating_from_maven.html#migmvn:migrating_deps) to understand the relation
* Find equivalent plugins for maven, [here are spring equivalents](https://docs.spring.io/spring-boot/maven-plugin/getting-started.html)
* Activate usage of javadoc for API documentation, see more of this file
* Substitute gradle automatic docker with maven equivalent or just use commands on file below

## Technologies used:
Technologies used:
* Spring Boot 4.0.1
* Spring Data JPA (Hibernate 7)
* Gradle kotlin
* Java 21
* JUnit 5
* Docker for database (latest PostgresSQL version)
* [Test containers](https://testcontainers.com/)
* [Spring docs](https://springdoc.org) 3.* version, automatic API documentation for spring 

Considered using [java faker](https://github.com/DiUS/java-faker/tree/master) for testing, due to its capabilities to
generate random values for database insertion, but including it makes Intelij in my computer not recognize
any gradle dependencies for reasons unknown.

### About spring docs

Access on browser the base url for the server used in the code plus the value found in springdoc.swagger-ui.path of the
[spring properties file](./src/main/resources/application.yaml). This will give you a page with every available endpoint
of your API. If this file is up to date, this project should use http://localhost:8080/swagger-ui.html by default.

https://deepwiki.com/springdoc/springdoc-openapi/8.3-javadoc-integration

### Javadoc

Gradle was set up so the javadoc documentation of the code is used. This project did it with uses gradle, but 
documentation shows example for [maven](https://springdoc.org/#javadoc-support). Javadoc may not cover well all
instances of the API documentation. For example, if your method on a rest controller returns ResponseEntity directly, 
and handles exceptions directly, I can assure from personal experience the browser documentation does not translate all
possible return statuses. For this you may use Open-API's (comes from transitive dependencies) 
[Operation annotation](https://github.com/OAI/OpenAPI-Specification/blob/3.0.4/versions/2.0.md#operation-object) to add
documentation besides javadoc.

Whether it is actually necessary at any time to use OpenApi is beyond my knowledge. After all, spring can be structured
to use [controllerAdvice](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-advice.html) 
to handle apart the exceptions. Spring has a lot of tools, and there may be some spring-docs can translate to 
documentation better.

## How to run this code

An instance of this server can be started with gradlew bootRun. Due to configuring the [gradle file](./build.gradle.kts)
to do so, a postgres database will start (created if needed) automatically. Stopping the database however, is only done
automatically if the code aborts due to an exception. When forcing the server to stop manually, the step to shut down 
the database is skipped.

When avoiding gradle to start and stop the database, use

```
docker compose -p book-lend -f ./docker-compose.yml up -d --build book-lending-app
```

to create the image, along with creating and start the container (uses already created), and

```
docker compose -p book-lend -f ./docker-compose.yml pause book-lending-app
```

to shut that container down.

## Database

Postgres is the chosen database. Its related files can be found [here](./src/main/resources/sql).

## Foreign API

To avoid users placing possibly bad information about books themselves, a foreign API about books is used.
https://openlibrary.org/dev/docs/api/search was used due to being free. https://developer.api.oclc.org is bigger and 
therefore preferable, but requires authentication given only to some organizations.

## Project structure

```
└── 📁src
    └── 📁main
        └── 📁docker
            ├── Dockerfile
        └── 📁java
            └── 📁backend
                └── 📁bookSharing
                    └── 📁http
                    └── 📁repository
                        └── 📁entities
                    └── 📁services
                        └── 📁book
                            └── 📁api
                        └── 📁user
                            └── 📁services
                    ├── Main.java
        └── 📁resources
            └── 📁sql
                ├── creation.sql
            ├── application.properties
    └── 📁test
```

Above is the structure of the src folder. The main and test folders are standard, while the resources/sql and docker 
folders serve for automation of the database creation, that can be activated with gradle.

### BookSharing

There are three main folders in this structure, the repository, the services and http. 

The base is the repository structure, which contains the database entities as classes, the translation being done with 
jakarta, and the base repository CRUD actions, something the spring does automatically by implementing specific 
interfaces with our generics.

Next step is the services folder, with the power of defining the restrictions on how to interact with the repository
data, such as defining transaction levels and making use of foreign APIs (used here to get information of books through
their isbn).

The last step is http, which will read and parse the data of receiving http requests and making use of the services 
layer to answer valid requests.

Outside of these folders is the main file. This has the boilerplate code to start a spring server app, but it's called to
attention that any component spring cannot create by itself will be supplemented with bean annotated methods. At this 
stage, the example of defining the validity time of the tokens is done here, and any such information that may be changed
should be placed here for ease of change.
