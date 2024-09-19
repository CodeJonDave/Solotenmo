package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class AuthenticationService {

    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public AuthenticationService(String url) {
        this.baseUrl = url;
    }

    public AuthenticatedUser login(UserCredentials credentials) {
        HttpEntity<UserCredentials> entity = createCredentialsEntity(credentials);
        AuthenticatedUser user = null;
        try {
            ResponseEntity<AuthenticatedUser> response = restTemplate.exchange(baseUrl + "login", HttpMethod.POST, entity, AuthenticatedUser.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                user = response.getBody();
                BasicLogger.info("User " + credentials.getUsername() + " logged in successfully.");
            }
        } catch (RestClientResponseException e) {
            BasicLogger.error("Login failed for user " + credentials.getUsername() + ": " + e.getRawStatusCode() + " " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.error("Resource access error during login for user " + credentials.getUsername() + ": " + e.getMessage());
        }

        if (user == null) {
            BasicLogger.info("Login failed for user " + credentials.getUsername());
        }
        return user;
    }

    public boolean register(UserCredentials credentials) {
        HttpEntity<UserCredentials> entity = createCredentialsEntity(credentials);
        try {
            ResponseEntity<Void> response = restTemplate.exchange(baseUrl + "register", HttpMethod.POST, entity, Void.class);
            if (response.getStatusCode() == HttpStatus.CREATED) {
                BasicLogger.info("New user registered: " + credentials.getUsername());
                return true;
            } else {
                BasicLogger.error("Unexpected status code during registration: " + response.getStatusCode());
            }
        } catch (RestClientResponseException e) {
            if (e.getRawStatusCode() == HttpStatus.BAD_REQUEST.value()) {
                System.out.println("Registration failed: Username already exists.");
            } else {
                BasicLogger.error("Registration failed for user " + credentials.getUsername() + ": " + e.getRawStatusCode() + " " + e.getStatusText());
            }
        } catch (ResourceAccessException e) {
            BasicLogger.error("Resource access error during registration for user " + credentials.getUsername() + ": " + e.getMessage());
        }
        return false;
    }

    private HttpEntity<UserCredentials> createCredentialsEntity(UserCredentials credentials) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(credentials, headers);
    }
}
