package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.exception.AccountAccessException;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.util.BasicLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final AccountDao accountDao;

    @Autowired
    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public Integer getAccountIdByUserId(int userId) {
        try {
            Integer accountId = accountDao.getAccountIdByUserId(userId);
            if (accountId == null) {
                BasicLogger.warn("No account Id found for user Id: " + userId);
            } else {
                BasicLogger.info("Account Id: " + accountId + " found for user: " + userId);
            }
            return accountId;
        } catch (DaoException ex) {
            BasicLogger.error("Error retrieving account Id with user Id: " + userId, ex);
            throw new AccountAccessException("Error retrieving account Id with user Id: " + userId);
        }
    }


}
