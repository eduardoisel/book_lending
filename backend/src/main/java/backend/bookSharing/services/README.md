# Organization

The code is divided between book and user related services. Each has its own interface and implementation, along with
its microservices.

# What is NOT done in this layer

## Authentication and Authorization check

Authentication and authorization is done by itself on the http layer, using filters for authorization and annotations
for authorization checks.

Authentication methods are made available in this layer, but the upper layer is the one required to use them.

# Basic data validation

Basic data violation refers to checks that can be done without access to the database. For example, ISBN have a specific
format, meaning a request can be detected as useless immediately. The same can be applied to null values, and
numbers that need to be in a specific interval.

If one decides to transfer the data validation to the service, there are 2 caveats to beware:
* the class itself (or interface) will need the <mark>org.springframework.validation.annotation.Validated</mark> to actually validate values found on parameters
* SpringDoc is expecting the annotations for documentation. This is an issue if annotations added directly on the parameter (such as a @Positive on a number) is moved, as it will only check the controller param and not check if the service will require more validations. One can create wrapper data class with the basic value, place the annotation there and receive it directly on the controller

## Return types

Uses the base of [declarative methods](https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative/rolling-back.html)
rolling back on exception.

# Todo

## String size checks

So far, there is no check for the upper limit of string sizes. This could lead to rogue exceptions being thrown. Spring
will cover unexpected exceptions with http code 500, which is to be avoided at all costs.