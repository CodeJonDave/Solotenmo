package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.exception.TransferAccessException;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.util.BasicLogger;
import com.techelevator.tenmo.util.ValidateId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class TransferService {
    private final TransferDao transferDao;
    private final TransferTypeService transferTypeService;
    private final TransferStatusService transferStatusService;
    private final AccountService accountService;
    private final UserService userService;
    private final BalanceService balanceService;


    @Autowired
    public TransferService(TransferDao transferDao, TransferTypeService transferTypeService, TransferStatusService transferStatusService, UserService userService, AccountService accountService, BalanceService balanceService) {
        this.transferDao = transferDao;
        this.transferTypeService = transferTypeService;
        this.transferStatusService = transferStatusService;
        this.accountService = accountService;
        this.userService = userService;
        this.balanceService = balanceService;
    }

    public TransferDto getTransferById(int transferId) {
        try {
            ValidateId.validateTransferId(transferId);
            TransferDto transfer = transferDao.getTransferById(transferId);
            if (transfer == null) {
                BasicLogger.warn("Transfer could not be located with transferId: " + transferId);
            } else {
                BasicLogger.info("Retrieved transfer info for transfer: " + transferId);
            }
            return transfer;
        } catch (IllegalArgumentException ex) {
            throw new TransferAccessException("The transfer id was invalid", ex);
        } catch (DaoException ex) {
            throw new TransferAccessException("Error fetching transfer by transferId: " + transferId, ex);
        }
    }


    public Integer createNewTransfer(TransferDto transferDto) {
        int userFrom = userService.getUserByName(transferDto.getAccount_from()).getId();
        int userTo = userService.getUserByName(transferDto.getAccount_to()).getId();

        int type = transferTypeService.getIdByDesc(transferDto.getTransferType());
        int status = transferStatusService.getIdByDesc(transferDto.getTransferStatus());

        int from = accountService.getAccountIdByUserId(userFrom);
        int to = accountService.getAccountIdByUserId(userTo);

        BigDecimal amount = transferDto.getAmount();

        try {
            Integer transferId = transferDao.createTransfer(type, status, from, to, amount);


            if (transferId == null) {
                BasicLogger.warn("The transfer was not created/returned");
            } else {
                if (transferDto.getTransferType().equals("Send")) {
                    int decreaseRows = balanceService.decreaseBalanceById(userFrom, amount);
                    int increaseRows = balanceService.increaseBalanceById(userTo, amount);
                }
                BasicLogger.info("Created and retrieved new transfer with id: " + transferId);
            }
            return transferId;
        } catch (DaoException ex) {
            throw new TransferAccessException("Error occurred while creating the transfer", ex);
        }
    }
}
