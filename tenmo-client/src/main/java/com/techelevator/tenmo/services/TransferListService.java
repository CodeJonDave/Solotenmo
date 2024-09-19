package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.util.AppConstants;
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

public class TransferListService {
    private final String transferListUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String token;


    public TransferListService(String baseUrl, String token) {
        this.transferListUrl = baseUrl + "transferList";
        this.token = token;
    }

    public List<TransferDto> getTransfers(AppConstants.ListType type) {
        if (token == null) {
            BasicLogger.error("Token is not set. Cannot retrieve transfers.");
            throw new IllegalStateException("Token is not set.");
        }

        HttpEntity<String> entity = HTTPEntityGenerator.generateAuthEntity(token);

        try {
            ResponseEntity<TransferDto[]> response = restTemplate.exchange(
                    transferListUrl + "/" + type.toString(),
                    HttpMethod.GET,
                    entity,
                    TransferDto[].class
            );

            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                BasicLogger.warn("The request for transfer history produced no results.");
                return new ArrayList<>();
            }

            List<TransferDto> transfers = Arrays.asList(Objects.requireNonNull(response.getBody()));

            BasicLogger.info("Retrieved users transfer history");
            return transfers;
        } catch (RestClientResponseException | ResourceAccessException ex) {
            BasicLogger.error("Error fetching the users transfer history");
            System.err.println("Error fetching transfer history: " + ex.getMessage());
            return new ArrayList<>();
        }
    }
}
