package com.techelevator.tenmo.model;


import java.math.BigDecimal;

public class TransferDto {
    private int transferId;
    private String transferType;
    private String transferStatus;
    private String account_from;
    private String account_to;
    private BigDecimal amount;

    public TransferDto() {
    }


    public TransferDto(int transferId, String transferType, String transferStatus, String account_from, String account_to, BigDecimal amount) {
        this.transferId = transferId;
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.account_from = account_from;
        this.account_to = account_to;
        this.amount = amount;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public String getAccount_from() {
        return account_from;
    }

    public void setAccount_from(String account_from) {
        this.account_from = account_from;
    }

    public String getAccount_to() {
        return account_to;
    }

    public void setAccount_to(String account_to) {
        this.account_to = account_to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    @Override
    public String toString() {
        return "Transfer Id: " + this.transferId + "\n" +
                "Transfer Type: " + this.transferType + "\n" +
                "Transfer Status: " + this.transferStatus + "\n" +
                "Account From: " + this.account_from + "\n" +
                "Account To: " + this.account_to + "\n" +
                "Amount: " + this.amount + "\n";
    }
}
