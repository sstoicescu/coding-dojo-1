Spring Boot Coding Dojo
---

### Introduction

This is a simple weather forwarder part of the coding dojo from https://github.com/marcosbarbero/coding-dojo-spring-boot

### Decisions

## Implemented functionality:

- Break down functionality into separate services (Data layer, weather API layer)
- Several types of integration and end to end tests
- Support multiple environments
- The application verifies its OpenWeather configuration is correct before declaring itself as ready to receive requests.
- There is a primitive rate limiter.

## Discussion around production readiness
 The dojo doesn't specify all the information required for full production readiness; more specifically, the target usage and the environment in which the application would be deployed.
 If it's a public service, we should have some form of authentication/authorization.
 Rate limiting is an issue; not just configuration-wise (suppose we have a Developer account and an Enterprise one) but also in terms of distribution: running instances on multiple machines. A workaround is to configure the API endpoint to a rate-limiter proxy.
 Secrets handling depends on the deployment strategy. At best, we want Spring Cloud Config or Spring Vault (and encrypting the API key at rest). The current implementation receives the values via the environment.


### The application
 The application depends on Spring Boot, Google Guava, as well as the Postgres connector.
There are 3 profiles available:
 - dev: uses an in-memory database, aimed at initial development and testing
 - int: uses a postgres connector, for more involved development work.
 - prod: uses a connector, and requires db credentials passed in during startup 


## Building & running 
 A standard Spring Boot application, build with:
```
./mvnw package
```

And start with 
```
SECRET_APP_ID=<your app id> ./mvnw run
```

 For the int environment, follow instructions on [Dockerhub](https://hub.docker.com/_/postgres) to get started. The stack.yml is what the application-int.properties files is configured with.
