package com.techelevator.tenmo.handler;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.BalanceService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.CreateTransferService;
import com.techelevator.tenmo.services.UserService;
import com.techelevator.tenmo.util.AppConstants;
import com.techelevator.tenmo.util.BasicLogger;
import com.techelevator.tenmo.util.ValidateSelection;

import java.math.BigDecimal;
import java.util.List;

public class CreateTransferHandler implements Handler {

    private User currentUser;
    private UserService userService;
    private ConsoleService consoleService;
    private final String baseUrl;
    private CreateTransferService createTransferService;
    private BalanceService balanceService;

    public CreateTransferHandler(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public void setAuthUser(AuthenticatedUser currentUser) {
        if (currentUser != null) {
            this.consoleService = new ConsoleService(currentUser.getUser());
            this.createTransferService = new CreateTransferService(baseUrl, currentUser.getToken());
            this.userService = new UserService(baseUrl, currentUser.getToken());
            this.balanceService = new BalanceService(baseUrl, currentUser.getToken());
            this.currentUser = currentUser.getUser();
        } else {
            BasicLogger.error("Cannot set null AuthenticatedUser in CreateTransferHandler.");
        }
    }

    public void createSendFunds() {
        handleCreateTransfer(AppConstants.Type.SEND);
    }


    public void createRequestFunds() {
        handleCreateTransfer(AppConstants.Type.REQUEST);
    }

    private void handleCreateTransfer(AppConstants.Type type) {
        try {
            consoleService.printNewTransferHeading(type);

            List<User> userList = userService.getListOfUsers();
            consoleService.printUserSelectMenu(userList);

            User selectedUser = handleSelectUser(userList, type);

            if (selectedUser == null) {
                System.out.println("Invalid user selection. Aborting transfer.");
                return;
            }

            BigDecimal amount = consoleService.promptForBigDecimal(
                    isSend(type) ?
                            "Enter the amount to send: " :
                            "Please enter the amount to request: "
            );

            if (isSend(type) && balanceService.getCurrentBalance().compareTo(amount) < 0) {
                System.out.println("You cannot send more funds than you have");
                return;
            }

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Amount must be greater than zero.");
                return;
            }

            TransferDto transferDto = createTransferDto(type, selectedUser, amount);
            int newTransferId = createTransferService.createNewTransfer(transferDto);
            if (newTransferId == 0) {
                System.out.println("Failed to create new transfer");
            } else {
                System.out.println("Created new " + type.toString() + " transfer: " + newTransferId);
            }
        } catch (Exception ex) {
            BasicLogger.error("Error occurred while creating transfer: " + ex.getMessage());
            System.out.println("An unexpected error occurred. Please try again.");
        }
    }

    private TransferDto createTransferDto(AppConstants.Type type, User selectedUser, BigDecimal amount) {
        TransferDto transferDto = new TransferDto();
        transferDto.setTransferType(type.toString());
        transferDto.setTransferStatus(isSend(type) ? AppConstants.Status.APPROVED.toString() : AppConstants.Status.PENDING.toString());
        transferDto.setAccount_from(isSend(type) ? currentUser.getUsername() : selectedUser.getUsername());
        transferDto.setAccount_to(isSend(type) ? selectedUser.getUsername() : currentUser.getUsername());
        transferDto.setAmount(amount);
        BasicLogger.info(transferDto.toString());
        return transferDto;
    }

    private User handleSelectUser(List<User> userList, AppConstants.Type type) {
        int userId = consoleService.promptForInt(
                isSend(type) ?
                        "Please enter the user Id you would like to transfer funds to: " :
                        "Please enter the user Id you would like to request funds from: "
        );

        if (!ValidateSelection.isUserIdValid(userId, userList)) {
            return null;
        }

        if (userId == currentUser.getId()) {
            System.out.println("You cannot send funds to yourself");
            return null;
        }

        return userList.stream()
                .filter(user -> user.getId() == userId)
                .findFirst()
                .orElseGet(() -> {
                    System.out.println("Could not locate user: " + userId);
                    return null;
                });
    }

    private boolean isSend(AppConstants.Type type) {
        return type == AppConstants.Type.SEND;
    }
}