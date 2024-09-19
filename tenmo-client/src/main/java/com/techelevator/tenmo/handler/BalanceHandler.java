package com.techelevator.tenmo.handler;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.services.BalanceService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.UserService;
import com.techelevator.tenmo.util.BasicLogger;

import java.math.BigDecimal;

public class BalanceHandler implements Handler {

    private ConsoleService consoleService;
    private final String baseUrl;
    private BalanceService balanceService;

    public BalanceHandler(String apiBaseUrl) {
        this.baseUrl = apiBaseUrl;
    }

    @Override
    public void setAuthUser(AuthenticatedUser currentUser) {
        if (currentUser != null) {
            this.consoleService = new ConsoleService(currentUser.getUser());
            this.balanceService = new BalanceService(baseUrl, currentUser.getToken());
        } else {
            BasicLogger.error("Cannot set null AuthenticatedUser in BalanceHandler.");
        }
    }

    public void getCurrentBalance() {
        if (balanceService == null) {
            BasicLogger.error("BalanceService is not initialized. Please set the authenticated user first.");
            consoleService.printErrorMessage();
            return;
        }

        BigDecimal balance = balanceService.getCurrentBalance();
        if (balance == null) {
            BasicLogger.warn("There was no balance retrieved");
            consoleService.printErrorMessage();
        } else {
            consoleService.printBalance(balance);
        }
    }
}