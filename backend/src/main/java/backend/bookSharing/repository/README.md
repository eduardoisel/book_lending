# JPA-Hibernate mapping

## One-to-many relations

Although it's mostly not used, fields from OneToOne, OneToMany and ManyToOne are placed. As far as i understand it is
impossible to do pagination with these fields, as a way to retrieve only partial information. Since the amount of tokens
associated to a user ought not to be more than 5 at any time, as long as code is set to delete older tokens that user 
may not delete by doing logout. This relation is the most appropriate to use OneToMany relation.

## Postgres enum

The base of this [database structure](../../../../resources/sql/creation.sql) is mostly basic, with the values translating
to String, numbers or java dates directly. The exception here is the usage of postgres enum for the [book's language](./entities/Book.java),
which requires more specialized annotations. Check [this site](https://www.baeldung.com/java-enums-jpa-postgresql)
if you want to downgrade the spring version, and consequently the hibernate version. Otherwise, substitute the sql file
to use a simple text for that field.

## Test containers compatibility

When using test containers, one does not have to use a table creation file as is done in [this project](../../../../resources).
One still has to indicate directly that the database will generate random values. This works well in serial ids, but
not on timestamp with default value of current timestamp. The most referenced I found for this was 
[@Generated](https://docs.hibernate.org/orm/6.1/javadocs/org/hibernate/annotations/GenerationTime.html#INSERT).
Other annotations seen so far (all from hibernate) were:
 * CurrentTimestamp
 * CreationTimestamp
 * ColumnDefault
 * DynamicInsert

Attempting the usage of hibernate **@CreationTimestamp(source = SourceType.DB)** on real time code with dockerfile
database, and updating Token table to not have default value **now()**, ensures timestamps are inserted (using logs, 
using **insert into token (created_at, last_used_at, user_id, token_validation) values(localtimestamp, localtimestamp, ?, ?) returning created_at, last_used_at**).
This leads me to believe testcontainers is simply incapable of recognizing the annotation.

If the solution is not found, to change database to not use default timestamp values

# JPA repositories

The implementation is done by the spring framework. As is seen on some of the interfaces, it is possible to add extra 
methods specific to the entity. Use [this official guide](https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html)
to explore what is possible.

# Json stringify for http

These entities may be shared directly to the server users. Annotations were used on fields that should not ever leave the
backend, but most importantly on one-to-many fields, to avoid recursive loops.
If in entity book one-to-many relation to ownership was not ignored, it would stringify the Owned entity, which would
stringify Book, which would again stringify its ownership field.

The alternative to these annotations is to never put these classes directly in the body, creating for each a new class
with only the necessary fields.

# lombok

Lombok is used do avoid boilerplate code, including every getter and setter for each field, along with no argument 
constructors [necessary for jakarta](https://www.baeldung.com/jpa-no-argument-constructor-entity-class)