package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

public interface BalanceDao {
    BigDecimal getBalanceById(int userId);
    int incrementBalanceById(int userId, BigDecimal amount);
    int decrementBalanceById(int userId, BigDecimal amount);
}
