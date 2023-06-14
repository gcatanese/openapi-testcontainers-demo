package com.example.user;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UserService {

    Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    HttpClient client;
    String uri;

    public UserService(String uri) {
        this.client = HttpClient.newHttpClient();
        this.uri = uri;
    }

    /**
     * Get user
     * @param id User identifier
     * @return
     * @throws Exception
     */
    public User get(String id) throws Exception {
        String endpoint = this.uri + "/users/" + id;
        LOGGER.info(endpoint);

        var request = HttpRequest.newBuilder(URI.create(endpoint))
                .header("accept", "application/json").GET().build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() != 200) {
            LOGGER.warn(response.body());
            throw new RuntimeException("An error has occurred. Http Status " + response.statusCode());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(response.body(), User.class);

        return user;
    }

    /**
     * Create new user
     * @param user
     * @return
     */
    public User create(User user) throws Exception {
        String endpoint = this.uri + "/users";

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String data = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);

        var request = HttpRequest.newBuilder(URI.create(endpoint))
                .header("accept", "application/json").POST(HttpRequest.BodyPublishers.ofString(data)).build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() != 200) {
            LOGGER.warn(response.body());
            throw new RuntimeException("An error has occurred. Http Status " + response.statusCode());
        }

        User newUser = objectMapper.readValue(response.body(), User.class);

        return newUser;
    }
}
