package com.techelevator.tenmo.util;

public class ValidateId {
    private static void validateId(Integer id, int minValue, String type) {
        if (id == null) {
            throw new IllegalArgumentException(type + " cannot be null.");
        }
        if (id < minValue) {
            throw new IllegalArgumentException(type + " must be greater than or equal to " + minValue);
        }
    }

    public static void validateStatusId(Integer statusId){
        if (statusId <= 0 || statusId > 3){
            throw new IllegalArgumentException("Status ID is invalid");
        }
    }

    public static void validateUserId(Integer userId) {
        validateId(userId, 1001, "User ID");
    }

    public static void validateAccountId(Integer accountId) {
        validateId(accountId, 2001, "Account ID");
    }

    public static void validateTransferId(Integer transferId) {
        validateId(transferId, 3001, "Transfer ID");
    }

    public static void validateTypeId(Integer typeId) {
        if (typeId < 1|| typeId > 2){
            throw new IllegalArgumentException("Transfer ID is invalid");
        }
    }
}