package com.techelevator.tenmo.handler;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.services.BalanceService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferApprovalService;
import com.techelevator.tenmo.util.BasicLogger;

import java.math.BigDecimal;

public class TransferApprovalHandler {

    static int selection = -1;

    private static TransferApprovalService transferApprovalService;
    private static BalanceService balanceService;
    private static final TransferDto currentTransfer = new TransferDto();

    public static void approveOrRejectTransfer(String baseUrl, TransferDto selectedTransfer, AuthenticatedUser currentUser) {
        transferApprovalService = new TransferApprovalService(baseUrl, currentUser.getToken());
        balanceService = new BalanceService(baseUrl, currentUser.getToken());
        ConsoleService consoleService = new ConsoleService(currentUser.getUser());

        currentTransfer.setTransferId(selectedTransfer.getTransferId());
        currentTransfer.setAmount(selectedTransfer.getAmount());

        consoleService.printCliBorder(25);
        consoleService.printApproveRejectMenu();

        selection = consoleService.promptForApprovalOption();
        currentTransfer.setTransferId(selectedTransfer.getTransferId());

        int transferUpdates = 0;

        try {
            switch (selection) {
                case 1:
                    currentTransfer.setAmount(selectedTransfer.getAmount());
                    currentTransfer.setAccount_to(selectedTransfer.getAccount_to());
                    transferUpdates = verifyAndApproveTransfer();
                    BasicLogger.info("Approved transfer: " + currentTransfer.getTransferId());
                    break;
                case 2:
                    transferUpdates = rejectTransferRequest();
                    BasicLogger.info("Rejected transfer: " + currentTransfer.getTransferId());
                    break;
                default:
                    System.out.println("Returning... ");
                    return;
            }

            if (selection == 1 && transferUpdates != 3) {
                System.out.println("There was an error in approving the transfer");
            } else if (selection == 2 && transferUpdates != 1) {
                System.out.println("There was an error in rejecting the transfer");
            } else {
                System.out.println("You have successfully " + (selection == 1 ? "approved " : "rejected ") + "transfer: " + selectedTransfer.getTransferId());
            }
        } catch (Exception e) {
            BasicLogger.error("Error occurred while processing transfer: " + e.getMessage());
            System.out.println("An unexpected error occurred. Please try again.");
        }
    }

    private static int verifyAndApproveTransfer() {
        try {
            BigDecimal balance = balanceService.getCurrentBalance();
            if (balance.compareTo(currentTransfer.getAmount()) < 0) {
                System.out.println("You can not approve a request for more funds than you have.");
                return 0;
            } else {
                return transferApprovalService.approveTransfer(currentTransfer);
            }
        } catch (Exception e) {
            BasicLogger.error("Error while approving transfer: " + e.getMessage());
            return 0;
        }
    }

    private static int rejectTransferRequest() {
        try {
            return transferApprovalService.rejectTransfer(currentTransfer);
        } catch (Exception e) {
            BasicLogger.error("Error while rejecting transfer: " + e.getMessage());
            return 0;
        }
    }
}
