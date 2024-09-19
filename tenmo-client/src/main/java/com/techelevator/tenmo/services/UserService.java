package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.util.BasicLogger;
import com.techelevator.tenmo.util.HTTPEntityGenerator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UserService {
    private final String userListUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String token;

    public UserService(String apiBaseUrl, String token) {
        this.userListUrl = apiBaseUrl + "user";
        this.token = token;
    }

    public List<User> getListOfUsers() {
        HttpEntity<String> entity = HTTPEntityGenerator.generateAuthEntity(token);
        try {
            ResponseEntity<User[]> response = restTemplate.exchange(
                    userListUrl,
                    HttpMethod.GET,
                    entity,
                    User[].class
            );
            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                BasicLogger.warn("The request for Users List produced no results.");
                return new ArrayList<>();
            }
            List<User> users = Arrays.asList(Objects.requireNonNull(response.getBody()));

            BasicLogger.info("Retrieved users");
            return users;

        } catch (RestClientResponseException | ResourceAccessException ex) {
            BasicLogger.error("Error fetching users");
            System.err.println("Error fetching users: " + ex.getMessage());
            return new ArrayList<>();
        }
    }
}
