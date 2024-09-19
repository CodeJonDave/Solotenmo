package com.techelevator.tenmo;

import com.techelevator.tenmo.handler.BalanceHandler;
import com.techelevator.tenmo.handler.CreateTransferHandler;
import com.techelevator.tenmo.handler.TransferListHandler;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;


public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final BalanceHandler balanceHandler = new BalanceHandler(API_BASE_URL);
    private final TransferListHandler transferListHandler = new TransferListHandler(API_BASE_URL);
    private final CreateTransferHandler createTransferHandler = new CreateTransferHandler(API_BASE_URL);


    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            initiateHandlers();
            mainMenu();
        }
    }

    private void initiateHandlers() {
        balanceHandler.setAuthUser(currentUser);
        transferListHandler.setAuthUser(currentUser);
        createTransferHandler.setAuthUser(currentUser);
        consoleService.setCurrentUser(currentUser.getUser());
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            System.out.println("Registration failed. Please try again.");
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        balanceHandler.getCurrentBalance();
    }

    private void viewTransferHistory() {
        transferListHandler.getFullTransferHistory();
    }

    private void viewPendingRequests() {
        transferListHandler.getPendingTransfers();
    }

    private void sendBucks() {
        createTransferHandler.createSendFunds();
    }

    private void requestBucks() {
        createTransferHandler.createRequestFunds();
    }
}
