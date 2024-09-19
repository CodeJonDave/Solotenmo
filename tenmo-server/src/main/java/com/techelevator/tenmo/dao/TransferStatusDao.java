package com.techelevator.tenmo.dao;

public interface TransferStatusDao {
        Integer getStatusIdByDesc(String desc);
        String getStatusDescById(int statusId);
        int updateTransferStatus(int transferId, int transferStatusId);
}
