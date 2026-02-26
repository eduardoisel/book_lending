# Organization

```
└── 📁authentication
└── 📁configuration
└── 📁controller
└── 📁data
└── 📁returns
```

The folders data and returns contain data classes received and returned through the http body, respectively.

The controller folder contains the app's spring controllers.

The authentication folder contains an authentication filter and an authentication entry point for bearer authentication.
These are used on SecurityConfiguration, the class that defines which API endpoints need to be authenticated.

# Caching

Implementation of caching using [CaffeineCacheManager](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/cache/caffeine/CaffeineCacheManager.html).
Used [this repo as starting example](https://github.com/abhi9720/BankingPortal-API). If one wants other caches, see  
[this link](https://medium.com/@alxkm/effective-cache-management-in-spring-boot-applications-22f06f92db70).

This implementation does not use http headers for cache at this point.

Annotation [Cacheable](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/cache/annotation/Cacheable.html)
and others related on the cached methods are used.

## Caching Implementation
When defining the [cache configuration](./configuration/CacheConfiguration.java), every single string set in the value
field of the related annotations must be added explicitly to the Caffeine cache manager. Not doing so will lead to an
exception being thrown when calling to that api endpoint.

Annotations have fields that use [spring Expression Language](https://docs.spring.io/spring-framework/reference/core/expressions.html).
You can use them to indicate when caching should not be applied. As an example, when browsing books (which cannot be
removed from the database, at least so far), the only condition that may deprecate a list of returned books is more books
being added, either leading to the page being completed or a next page being created. With these fields, we can say the cache
should not save the value if the ListedData.hasNextPage() method call is false.