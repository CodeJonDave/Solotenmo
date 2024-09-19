package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.TransferTypeDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.exception.TransferTypeDescAccessException;
import com.techelevator.tenmo.exception.TransferTypeIdAccessException;
import com.techelevator.tenmo.util.BasicLogger;
import com.techelevator.tenmo.util.SQLInjectionCheck;
import com.techelevator.tenmo.util.ValidateId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferTypeService {
    public final TransferTypeDao transferTypeDao;

    @Autowired
    public TransferTypeService(TransferTypeDao transferTypeDao) {
        this.transferTypeDao = transferTypeDao;
    }

    public String getDescById(Integer typeId) {
        String description = null;
        try {
            ValidateId.validateTypeId(typeId);
            description = transferTypeDao.getTypeDescById(typeId);
            if (description == null) {
                BasicLogger.warn("Failed to retrieve the type description for Id: " + typeId);
            } else {
                BasicLogger.info("Retrieved description for type Id: " + typeId);
            }
        } catch (IllegalArgumentException ex) {
            throw new TransferTypeIdAccessException("The id entered was invalid: " + typeId, ex);
        } catch (DaoException ex) {
            throw new TransferTypeIdAccessException("There was an error retrieving the type with Id: " + typeId, ex);
        }
        return description;
    }

    public Integer getIdByDesc(String description) {
        int typeId = 0;
        try {
            if (!SQLInjectionCheck.isSafe(description)) {
                throw new IllegalArgumentException("The description: " + description + "was invalid");
            }
            typeId = transferTypeDao.getTransferTypeByDesc(description);
            if (typeId == 0) {
                BasicLogger.warn("There was no id returned for description: " + description);
            } else {
                BasicLogger.info("Retrieved status Id for description: " + description);
            }
            return typeId;
        } catch (DaoException ex) {
            throw new TransferTypeDescAccessException("There was an error retrieving the type id for description: " + description, ex);
        } catch (IllegalArgumentException ex) {
            throw new TransferTypeDescAccessException("The description: " + description + " contained potential SQL injection", ex);
        }
    }
}
