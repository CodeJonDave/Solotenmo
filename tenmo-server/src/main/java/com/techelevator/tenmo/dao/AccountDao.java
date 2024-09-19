package com.techelevator.tenmo.dao;


public interface AccountDao {
    Integer getAccountIdByUserId(int id);
    Integer getAccountIdByUsername(String username);
}
