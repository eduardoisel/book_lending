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

## Return types

Uses the base of [declarative methods](https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative/rolling-back.html)
rolling back on exception.

# Todo

## String size checks

So far, there is no check for the upper limit of string sizes. This could lead to rogue exceptions being thrown. Spring
will cover unexpected exceptions with http code 500, which is to be avoided at all costs.