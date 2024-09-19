package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferDto;

import java.util.List;

public interface TransferListDao {
    List<TransferDto> getTransferHistory(int userId);
    List<TransferDto> getPendingTransfers(int userId);
}
