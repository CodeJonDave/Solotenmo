package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.util.BasicLogger;
import com.techelevator.tenmo.util.HTTPEntityGenerator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class CreateTransferService {

    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String token;

    public CreateTransferService(String apiBaseUrl, String token) {
        this.baseUrl = apiBaseUrl + "transfer";
        this.token = token;
    }


    public int createNewTransfer(TransferDto newTransfer) {
        if (token == null) {
            BasicLogger.error("Token is not set. Cannot create transfers.");
            throw new IllegalStateException("Token is not set.");
        }

        HttpEntity<TransferDto> entity = HTTPEntityGenerator.generateEntityWithBody(token, newTransfer);
        try {
            ResponseEntity<Integer> response = restTemplate.exchange(
                    baseUrl,
                    HttpMethod.POST,
                    entity,
                    Integer.class
            );

            if (response.getBody() == null || response.getBody() == 0) {
                BasicLogger.error("There was an error creating the new transfer.");
                return 0;
            }
            return response.getBody();
        } catch (RestClientResponseException | ResourceAccessException ex) {
            BasicLogger.error("Error fetching the users transfer history");
            System.err.println("Error fetching transfer history: " + ex.getMessage());
            return 0;
        }
    }
}

