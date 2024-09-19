package com.techelevator.tenmo.handler;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferListService;
import com.techelevator.tenmo.util.AppConstants;
import com.techelevator.tenmo.util.BasicLogger;
import com.techelevator.tenmo.util.ValidateSelection;

import java.util.List;

public class TransferListHandler implements Handler {

    private ConsoleService consoleService;
    private final String baseUrl;
    private TransferListService transferListService;
    private AuthenticatedUser currentUser;

    public TransferListHandler(String apiBaseUrl) {
        this.baseUrl = apiBaseUrl;
    }

    @Override
    public void setAuthUser(AuthenticatedUser currentUser) {
        if (currentUser != null) {
            this.consoleService = new ConsoleService(currentUser.getUser());
            this.transferListService = new TransferListService(baseUrl, currentUser.getToken());
            this.currentUser = currentUser;
        } else {
            BasicLogger.error("Cannot set null AuthenticatedUser in TransferListService.");
        }
    }

    public void getFullTransferHistory() {
        List<TransferDto> allTransfers = getTransfers(AppConstants.ListType.FULL);
        if (allTransfers == null || allTransfers.isEmpty()) {
            return;
        }
        handleViewingTransfers(AppConstants.ListType.FULL, allTransfers);
        TransferDto selectedTransfer = handleTransferSelection(AppConstants.ListType.FULL, allTransfers);
        if (selectedTransfer != null) {
            consoleService.printTransferDetails(selectedTransfer);
        }
    }

    public void getPendingTransfers() {
        List<TransferDto> pendingTransfers = getTransfers(AppConstants.ListType.PENDING);
        if (pendingTransfers == null || pendingTransfers.isEmpty()) {
            return;
        }
        handleViewingTransfers(AppConstants.ListType.PENDING, pendingTransfers);
        TransferDto selectedTransfer = handleTransferSelection(AppConstants.ListType.PENDING, pendingTransfers);
        if (selectedTransfer != null) {
            consoleService.printTransferDetails(selectedTransfer);
            TransferApprovalHandler.approveOrRejectTransfer(baseUrl, selectedTransfer, currentUser);
        }

    }

    public List<TransferDto> getTransfers(AppConstants.ListType type) {
        if (transferListService == null) {
            BasicLogger.error("TransferService is not initialized. Please set the authenticated user first.");
            consoleService.printErrorMessage();
            return null;
        }

        List<TransferDto> transfers = null;
        transfers = transferListService.getTransfers(type);

        if (transfers == null || transfers.isEmpty()) {
            BasicLogger.error("There was an error retrieving your: " + type.toString() + " transfers");
            System.out.println("There were no " + type.toString() + " transfers found.");
        } else {
            BasicLogger.info("Retrieved " + type.toString() + " Transfers");
        }
        return transfers;
    }


    private void handleViewingTransfers(AppConstants.ListType type, List<TransferDto> transfers) {
        consoleService.printCliBorder(50);
        if (type.equals(AppConstants.ListType.FULL)) {
            consoleService.printHistoryHeader();
        } else {
            consoleService.printPendingHeader();
        }
        consoleService.printCliBorder(50);

        for (TransferDto transfer : transfers) {
            if (type.equals(AppConstants.ListType.FULL)) {
                consoleService.printFullHistoryTransfer(transfer, currentUser.getUser().getUsername());
            } else {
                consoleService.printPendingTransferHistory(transfer);
            }
        }
        consoleService.printCliBorder(25);
    }

    private TransferDto handleTransferSelection(AppConstants.ListType type, List<TransferDto> transfers) {
        int transferId = consoleService.promptForInt(
                type.equals(AppConstants.ListType.FULL) ?
                        "Please enter the Transfer Id to view transfer details (enter 0 to return): " :
                        "Please enter the Transfer Id to approve/reject (enter 0 to return): "
        );

        if (!ValidateSelection.isTransferIdValid(transferId, transfers)) {
            return null;
        }

        return transfers.stream()
                .filter(transfer -> transfer.getTransferId() == transferId)
                .findFirst()
                .orElse(null);
    }


}