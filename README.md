# OpenAPI Testcontainers Demo
Demo of the OpenAPI Testcontainers extension

## Overview

This repository includes a sample on how to use the [OpenAPI Testcontainers](https://github.com/gcatanese/openapi-testcontainers) extension.

The `UserService` class includes methods to manage users invoking a REST API.  
The `UserServiceTest` implements the unit testing where Testcontainers is used to create a container with a mock 
server of the API being used.

### Usage

```java
// define path to OpenAPI file
private final static String OPENAPI_FILE = "src/test/resources/users-openapi.yaml";

// load container
@ClassRule
public static GenericContainer container = new GenericContainer(
        new ImageFromDockerfile("my-test-cont", false)
        .withFileFromFile("openapi.yaml", new File(OPENAPI_FILE))
        .withFileFromFile("Dockerfile", new File("Dockerfile"))
        )
        .withExposedPorts(8080);
```
### How it works

The Testcontainer is created on-the-fly from the OpenAPI specification (ie `users-openapi.yaml`).




