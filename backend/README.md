# About this branch

The only base change from the main branch is using an embedded H2 database instead of a Postgres
instance on the docker. As with test containers, it may have issues with default values. Such an issue 
can be resolved by creating them on code.

## TODO

Admin user capabilities may also be added, allowing special operations like suspending/banning users, such as 
harassment through any communication in the server or adding fake books. The damaging books or failing to return them
to their owners after the time limit will also be added, even if in this person to person lending framework may not 
allow any realistic way for these cases to be actually verified.

Testing framework is still being developed, such as:
* kinks with test containers to be solved, seems it cannot generate default values such as timestamps.
* [this file's](src/test/java/commons/backend/bookSharing/TestData.java) is inserted on the beginning of a lot of tests each time, to avoid common inserts. They will not hold the auto generated id, and sometimes it will act against testing interests.

Http capabilities are underdeveloped. Looking into adding cache and possibly using hateoas.

A real implementation of this service would also use better tools to judge the proximity of users, as this is a service
about physically lending books. Unless it just saves user input coordinates and judge distance by ignoring actual
terrain such as oceans, a significant external service would be needed.

Smaller things to solve on current code may be marked with todo text. 

## Conversion to maven

If you do not like gradle you will need to:

* Change the dependencies, see [here](https://docs.gradle.org/current/userguide/migrating_from_maven.html#migmvn:migrating_deps) to understand the relation
* Find equivalent plugins for maven, [here are spring equivalents](https://docs.spring.io/spring-boot/maven-plugin/getting-started.html)
* Activate usage of javadoc for API documentation, see more of this file

There is also the issue of testing, this setup adds besides the default main and testing folders, the integration test
folder, one used to ensure unit tests run before integration tests. If one is not interested in such a feature, simply
ignore the lines on the build gradle related to them, and change [the test folder](./src/test/java) by combining all the
direct child folders.

## Technologies used:
Technologies used:
* Spring Boot 4.0.1
* Spring Data JPA (Hibernate 7)
* Gradle kotlin
* Java 21
* JUnit 5
* [Test containers](https://testcontainers.com/)
* [Spring docs](https://springdoc.org) 3.* version, automatic API documentation for spring. Check [the limitations of its current setting](#Spring-docs-limitations) before using it

Considered using [java faker](https://github.com/DiUS/java-faker/tree/master) for testing, due to its capabilities to
generate random values for database insertion, but including it makes Intelij in my computer not recognize
any gradle dependencies for reasons unknown.



### About spring docs

Access on browser the base url for the server used in the code plus the value found in springdoc.swagger-ui.path of the
[spring properties file](./src/main/resources/application.yaml). This will give you a page with every available endpoint
of your API. If this file is up to date, this project should use http://localhost:8080/swagger-ui.html by default.

#### Spring docs limitations

At least as it is configured right now, endpoint parameters are based on the parameters the corresponding method uses,
along with the option to use authentication (set to use Bearer authentication), which will always appear regardless of
it being necessary. This leads to 2 problems, both related to the parameters each endpoint will need. 

The first issue being spring http is set to use a custom argument resolver for user information. 
This means on places where the user needs to be authenticated, spring is set to be able to grab the information from the
authentication, automatically allowing Controller methods to use the user information as its parameter. Spring docs
will say that setting that information is required, but this is not true, as the server will completely ignore that info
and retrieve it from the authentication.

The second is related to http caching. As explained [on the http md](./src/main/java/backend/bookSharing/http/README.md),
http get request can use the header "If-None-Match", receiving a not modified status response if the value is valid.
Again, spring docs will not tell you this since it is done by a filter, instead of the controller methods.

### Javadoc

//https://deepwiki.com/springdoc/springdoc-openapi/8.3-javadoc-integration

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

## Database

Embedded H2 database, with the tables created automatically. on [this sql file](src/main/resources/data.sql)
one can insert at runtime the starting values. The data will reset on each app start

## Foreign API

To avoid users placing possibly bad information about books themselves, a foreign API about books is used.
https://openlibrary.org/dev/docs/api/search was used due to being free. https://developer.api.oclc.org is bigger and 
therefore preferable, but requires authentication given only to some organizations.

## Project structure

```
└── 📁src
    └── 📁main
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
