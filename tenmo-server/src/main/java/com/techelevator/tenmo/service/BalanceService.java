package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.BalanceDao;
import com.techelevator.tenmo.exception.BalanceAccessException;
import com.techelevator.tenmo.exception.BalanceUpdateException;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.util.BasicLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class BalanceService {

    private final BalanceDao balanceDao;
    private final UserService userService;

    @Autowired
    public BalanceService(BalanceDao balanceDao, UserService userService) {
        this.balanceDao = balanceDao;
        this.userService = userService;
    }

    public BigDecimal retrieveBalance() {
        try {
            User currentUser = userService.getCurrentUser();
            int userId = currentUser.getId();
            BigDecimal balance = balanceDao.getBalanceById(userId);
            if (balance == null) {
                BasicLogger.warn("No balance found for user: " + userId);
            } else {
                BasicLogger.info("Balance for user: " + userId + " retrieved successfully.");
            }
            return balance;
        } catch (DaoException ex) {
            throw new BalanceAccessException("Error retrieving balance for current user", ex);
        }
    }

    @Transactional
    public int increaseBalanceById(int userId, BigDecimal amount) {
        return updateBalance(userId, amount, true);
    }

    @Transactional
    public int decreaseBalanceById(int userId, BigDecimal amount) {
        return updateBalance(userId, amount, false);
    }


    private int updateBalance(int userId, BigDecimal amount, boolean isIncrease) {
        validateUserId(userId);
        validateAmount(amount);

        try {
            int rowsAffected = isIncrease
                    ? balanceDao.incrementBalanceById(userId, amount)
                    : balanceDao.decrementBalanceById(userId, amount);

            logBalanceUpdate(isIncrease, userId, amount, rowsAffected);
            return rowsAffected;
        } catch (DaoException ex) {
            String action = isIncrease ? "increasing" : "decreasing";
            throw new BalanceUpdateException("Error " + action + " balance for user: " + userId, ex);
        }
    }


    private void validateUserId(int userId) {
        if (userId <= 0) {
            throw new BalanceUpdateException("Invalid user ID: " + userId);
        }
    }


    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BalanceUpdateException("Invalid balance amount: " + amount);
        }
    }

    private void logBalanceUpdate(boolean isIncrease, int userId, BigDecimal amount, int rowsAffected) {
        String action = isIncrease ? "increased" : "decreased";
        if (rowsAffected != 1) {
            BasicLogger.warn("No effect on balance for userId: " + userId);
        } else {
            BasicLogger.info("Balance for userId: " + userId + " was " + action + " by: $" + amount);
        }
    }
}
