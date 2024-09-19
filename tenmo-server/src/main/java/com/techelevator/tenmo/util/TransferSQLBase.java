package com.techelevator.tenmo.util;

public class TransferSQLBase {
    public static final String TRANSFER_BASE = "SELECT t.transfer_id, " +
            "tt.transfer_type_desc AS transfer_type, " +
            "ts.transfer_status_desc AS transfer_status, " +
            "u_from.username AS account_from_username, " +
            "u_to.username AS account_to_username, " +
            "t.amount " +
            " FROM transfer t " +
            "JOIN transfer_type tt ON t.transfer_type_id = tt.transfer_type_id " +
            "JOIN transfer_status ts ON t.transfer_status_id = ts.transfer_status_id " +
            "JOIN account a_from ON t.account_from = a_from.account_id " +
            "JOIN account a_to ON t.account_to = a_to.account_id " +
            "JOIN tenmo_user u_from ON a_from.user_id = u_from.user_id " +
            "JOIN tenmo_user u_to ON a_to.user_id = u_to.user_id ";
}
