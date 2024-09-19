package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.TransferDto;

import java.math.BigDecimal;

public interface TransferDao {

    TransferDto getTransferById(int transferId);

    Integer createTransfer(int typeId, int statusId, int accountTo, int accountFrom, BigDecimal amount);
}
