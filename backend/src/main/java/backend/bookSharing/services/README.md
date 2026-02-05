# Organization

The code is divided between book and user related services. Each has its own interface and implementation, along with
its microservices and data classes.

## Return types

Uses the base of [declarative methods](https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative/rolling-back.html)
rolling back on exception.

### String size

So far, there is no check for the upper limit of string sizes. This could lead to rogue exceptions being thrown. Spring
will cover unexpected exceptions with http code 500, which is to be avoided at all costs.