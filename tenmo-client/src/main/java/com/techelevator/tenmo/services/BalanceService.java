package com.techelevator.tenmo.services;

import com.techelevator.tenmo.util.BasicLogger;
import com.techelevator.tenmo.util.HTTPEntityGenerator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class BalanceService {

    private final String balanceUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String token;

    public BalanceService(String baseUrl, String token) {
        this.balanceUrl = baseUrl + "balance";
        this.token = token;
    }

    public BigDecimal getCurrentBalance() {

        if (token == null) {
            BasicLogger.error("Token is not set. Cannot retrieve balance.");
            throw new IllegalStateException("Token is not set.");
        }

        HttpEntity<String> entity = HTTPEntityGenerator.generateAuthEntity(token);

        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(
                    balanceUrl,
                    HttpMethod.GET,
                    entity,
                    BigDecimal.class
            );
            BigDecimal balance = response.getBody();
            BasicLogger.info("Retrieved user balance");
            return balance;
        } catch (RestClientException ex) {
            BasicLogger.error("Error fetching balance for user: " + ex.getMessage());
            System.err.println("Error fetching balance: " + ex.getMessage());
            return null;
        }

    }
}
