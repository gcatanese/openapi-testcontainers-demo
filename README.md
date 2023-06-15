# OpenAPI Testcontainers Demo
Demo of the OpenAPI Testcontainers extension

## Overview

This repository includes a sample on how to use the [OpenAPI Testcontainers](https://github.com/gcatanese/openapi-testcontainers) extension.

The `UserService` class includes the methods to manage users invoking a REST API.  
The `UserServiceTest` implements the unit testing where Testcontainer is used to create a container with a mock 
server of the API.

The Testcontainer is created on-the-fly from the OpenAPI specification (ie `users-openapi.yaml`).

### Usage

Define the container and the path to the OpenAPI file in the test classes:
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
Make sure the [Dockerfile](https://github.com/gcatanese/openapi-testcontainers-demo/blob/main/Dockerfile) is available in your project.

**Note**: the first execution might take some time in order to download the base image. The actual Testcontainers image is instead pretty small and it is rebuilt only when the OpenAPI specification changes. 

### How it works

The extension is designed to fullfil the **Contract Testing** approach: the OpenAPI specification describes the endpoints and payloads
produced and consumed by the API. The OpenAPI examples, found in the file, define which request triggers a given response.
```
/users:
    post:
      summary: Create user
      requestBody:
        content:
          application/json:
             # request examples
            examples:
              example-user-eur:
                $ref: '#/components/examples/create-user-eur'
              example-user-us:
                $ref: '#/components/examples/create-user-us'
    responses:
        '200':
          content:
            application/json:
              # response examples
              examples:
                create-user-1:
                  $ref: '#/components/examples/create-user-eur-200'
                create-user-2:
                  $ref: '#/components/examples/create-user-us-200'        
```
Do you want to know how the interactions (request-response matching) are generated?
Find out more in [Contract Testing with OpenAPI](https://medium.com/geekculture/contract-testing-with-openapi-42267098ddc7) and
visit the [OpenAPI Testcontainers](https://github.com/gcatanese/openapi-testcontainers) repository.





