package com.example.user;

import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
public class UserServiceTest {

    Logger LOGGER = LoggerFactory.getLogger(UserServiceTest.class);

    UserService userService;

    // define path to OpenAPI file
    private final static String OPENAPI_FILE = "src/test/resources/users-openapi.yaml";

    @ClassRule
    public static GenericContainer container = new GenericContainer(
            new ImageFromDockerfile("my-test-cont", false)
                    .withFileFromFile("openapi.yaml", new File(OPENAPI_FILE))
                    .withFileFromFile("Dockerfile", new File("Dockerfile"))
    )
            .withExposedPorts(8080);

    @Before
    public void setUp() {
        Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(LOGGER);
        container.followOutput(logConsumer);

        var uri = "http://" + container.getHost() + ":" + container.getMappedPort(8080);
        userService = new UserService(uri);
    }

    @Test
    public void getUser() throws Exception {
        User user = userService.get("usr0001");

        assertEquals("usr0001", user.getId());
        assertEquals("Alan", user.getFirstName());
    }

    @Test
    public void getAnotherUser() throws Exception {
        User user = userService.get("usr0099");

        assertEquals("Rod", user.getFirstName());
    }

    @Test(expected = RuntimeException.class)
    public void userNotFound() throws Exception {
        User user = userService.get("usr9999");
    }

    @Test
    public void createUser() throws Exception {
        User user = new User();
        user.setFirstName("Rosco");
        user.setLastName("Irvine");
        user.setEmail("r.irvine@example.com");

        user = userService.create(user);

        assertNotNull(user.getId());
        assertEquals("newuser-010", user.getId());
    }

}
