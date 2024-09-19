package com.techelevator.tenmo.dao;

public interface TransferTypeDao {
    int getTransferTypeByDesc(String desc);
    String getTypeDescById(int typeId);
}
