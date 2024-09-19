package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.TransferStatusDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.exception.TransferStatusDescAccessException;
import com.techelevator.tenmo.exception.TransferStatusIdAccessException;
import com.techelevator.tenmo.util.BasicLogger;
import com.techelevator.tenmo.util.SQLInjectionCheck;
import com.techelevator.tenmo.util.ValidateId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferStatusService {

    private final TransferStatusDao transferStatusDao;

    @Autowired
    public TransferStatusService(TransferStatusDao transferStatusDao) {
        this.transferStatusDao = transferStatusDao;
    }

    public String getDescByID(int statusId) {
        String description = null;
        try {
            ValidateId.validateStatusId(statusId);
            description = transferStatusDao.getStatusDescById(statusId);
            if (description == null) {
                BasicLogger.warn("Failed to retrieve the status description for Id: " + statusId);
            } else {
                BasicLogger.info("Retrieved description for status Id: " + statusId);
            }
        } catch (IllegalArgumentException ex) {
            throw new TransferStatusIdAccessException("The id entered was invalid: " + statusId, ex);
        } catch (DaoException ex) {
            throw new TransferStatusIdAccessException("There was an error retrieving the status with Id: " + statusId);
        }
        return description;
    }


    public Integer getIdByDesc(String description) {
        Integer statusId = null;
        try {
            if (!SQLInjectionCheck.isSafe(description)) {
                throw new IllegalArgumentException("The description: " + description + "was invalid");
            }
            statusId = transferStatusDao.getStatusIdByDesc(description);
            if (statusId == 0) {
                BasicLogger.warn("There was no id returned for description: " + description);
            } else {
                BasicLogger.info("Retrieved status Id for description: " + description);
            }
            return statusId;
        } catch (DaoException ex) {
            throw new TransferStatusDescAccessException("There was an error retrieving the status id for description: " + description);
        } catch (IllegalArgumentException ex) {
            throw new TransferStatusDescAccessException("The description: " + description + " contained potential SQL injection");
        }
    }

    public Integer updateTransferStatus(int transferId, int transferStatusId) {
        Integer rowsAffected = null;
        try {
            ValidateId.validateTransferId(transferId);
            ValidateId.validateStatusId(transferStatusId);

            rowsAffected = transferStatusDao.updateTransferStatus(transferId, transferStatusId);
            if (rowsAffected == 1) {
                BasicLogger.info("Transfer with Id: " + transferId + " was successfully updated to ");
                return rowsAffected;
            } else {
                BasicLogger.warn("Unexpected number of rows: " + rowsAffected + " were updated with Id: " + transferStatusId);
            }
            return rowsAffected;
        } catch (IllegalArgumentException ex) {
            throw new TransferStatusDescAccessException("The transferId: " + transferId + " or transfer status Id: " + transferStatusId + " was invalid", ex);
        } catch (DaoException ex) {
            throw new TransferStatusDescAccessException("There was an error when updating the status for transfer: " + transferId, ex);
        }
    }
}
