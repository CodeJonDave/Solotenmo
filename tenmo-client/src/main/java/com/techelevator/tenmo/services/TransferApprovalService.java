package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.util.AppConstants;
import com.techelevator.tenmo.util.BasicLogger;
import com.techelevator.tenmo.util.HTTPEntityGenerator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferApprovalService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl;
    private final String token;

    public TransferApprovalService(String baseUrl, String token) {
        this.baseUrl = baseUrl + "transfer/";
        this.token = token;
    }

    public Integer approveTransfer(TransferDto selectedTransfer) {
        return processTransfer(selectedTransfer, AppConstants.Status.APPROVED);
    }

    public int rejectTransfer(TransferDto selectedTransfer) {
        return processTransfer(selectedTransfer, AppConstants.Status.REJECTED);
    }

    private int processTransfer(TransferDto selectedTransfer, AppConstants.Status action) {
        if (token == null) {
            BasicLogger.error("Token is not set. Cannot proceed with transfer.");
            throw new IllegalStateException("Token is not set.");
        }

        HttpEntity<TransferDto> entity = HTTPEntityGenerator.generateEntityWithBody(token, selectedTransfer);

        try {
            ResponseEntity<Integer> response = restTemplate.exchange(
                    baseUrl + "/" + action.toString(),
                    HttpMethod.PUT,
                    entity,
                    Integer.class
            );
            return response.getBody();
        } catch (RestClientResponseException | ResourceAccessException | NullPointerException ex) {
            BasicLogger.error("Error " + action + "ing the transfer: " + ex.getMessage());
            System.err.println("Error " + action + "ing the transfer: " + ex.getMessage());
            return 0;
        }
    }
}
