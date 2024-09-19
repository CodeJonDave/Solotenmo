package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.TransferListDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.exception.TransferListAccessException;
import com.techelevator.tenmo.model.TransferDto;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.util.BasicLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferListService {

    public enum TransferListType {
        PENDING, ALL
    }

    private final TransferListDao transferListDao;
    private final UserService userService;

    @Autowired
    public TransferListService(TransferListDao transferListDao, UserService userService) {
        this.transferListDao = transferListDao;
        this.userService = userService;
    }

    public List<TransferDto> getTransferHistory() {
        return retrieveTransfers(TransferListType.ALL);
    }

    public List<TransferDto> getPendingTransfers() {
        return retrieveTransfers(TransferListType.PENDING);
    }

    private List<TransferDto> retrieveTransfers(TransferListType transferListType) {
        try {
            User user = userService.getCurrentUser();
            int userId = user.getId();

            List<TransferDto> transfers;

            switch (transferListType) {
                case PENDING:
                    transfers = transferListDao.getPendingTransfers(userId);
                    break;
                case ALL:
                default:
                    transfers = transferListDao.getTransferHistory(userId);
                    break;
            }

            if (transfers.isEmpty()) {
                BasicLogger.warn("No " + transferListType.name().toLowerCase() + " transfers found.");
            } else {
                BasicLogger.info("Retrieved " + transferListType.name().toLowerCase() + " transfers for user: " + userId);
            }
            return transfers;
        } catch (DaoException ex) {
            throw new TransferListAccessException("Error fetching the " + transferListType.name().toLowerCase() + " transfers for user", ex);
        }
    }

}
