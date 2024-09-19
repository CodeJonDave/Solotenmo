package com.techelevator.tenmo.service;

import com.techelevator.tenmo.model.TransferDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransferUpdateService {
    private final String APPROVE = "Approved";
    private final String REJECT = "Rejected";

    private final TransferStatusService transferStatusService;
    private final BalanceService balanceService;
    private final UserService userService;


    @Autowired
    public TransferUpdateService(TransferStatusService transferStatusService, BalanceService balanceService, UserService userService) {
        this.transferStatusService = transferStatusService;
        this.balanceService = balanceService;
        this.userService = userService;
    }

    public Integer approveTransfer(TransferDto transfer) {
        int transferStatusId = transferStatusService.getIdByDesc(APPROVE);
        int userTo = userService.getUserByName(transfer.getAccount_to()).getId();
        int userFrom = userService.getCurrentUser().getId();
        BigDecimal amount = transfer.getAmount();

        int updatedLines = 0;
        updatedLines += transferStatusService.updateTransferStatus(transfer.getTransferId(), transferStatusId);
        updatedLines += balanceService.increaseBalanceById(userTo, amount);
        updatedLines += balanceService.decreaseBalanceById(userFrom, amount);

        return updatedLines;

    }

    public Integer rejectTransfer(TransferDto transfer) {
        int transferStatusId = transferStatusService.getIdByDesc(REJECT);
        return transferStatusService.updateTransferStatus(transfer.getTransferId(),transferStatusId);
    }
}
