# OpenAPI Testcontainers Demo
Demo of the OpenAPI Testcontainers extension

## Overview

This repository includes a sample on how to use the [OpenAPI Testcontainers](https://github.com/gcatanese/openapi-testcontainers) extension.

The `UserService` class includes the methods to manage users invoking a REST API.  
The `UserServiceTest` implements the unit testing where Testcontainer is used to create a container with a mock 
server of the API.

The Testcontainer is created on-the-fly from the OpenAPI specification (ie `users-openapi.yaml`).

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

The extension applies the **Contract Testing** guidelines: the OpenAPI specification describes the endpoints and payloads
produced and consumed by the API. Using the OpenAPI examples defined in the file the (mock) API returns, for a given request, 
the matching response.

Do you want to know how the interactions (request-response matching) are generated?
Find out more in [Contract Testing with OpenAPI](https://medium.com/geekculture/contract-testing-with-openapi-42267098ddc7) and
visit the [OpenAPI Testcontainers](https://github.com/gcatanese/openapi-testcontainers) repository.





