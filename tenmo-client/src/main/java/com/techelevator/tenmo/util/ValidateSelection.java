package com.techelevator.tenmo.util;

import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.model.User;

import java.util.List;

public class ValidateSelection {

    public static boolean isTransferIdValid(int transferId, List<TransferDto> transfers) {
        return isValidId(transferId, AppConstants.TRANSFER_MIN, transfers, TransferDto::getTransferId);
    }

    public static boolean isUserIdValid(int userId, List<User> userList) {
        return isValidId(userId, AppConstants.USER_MIN, userList, User::getId);
    }

    private static <T> boolean isValidId(int id, int minId, List<T> list, IdExtractor<T> extractor) {
        if (id < minId) {
            return false;
        }
        for (T item : list) {
            if (extractor.getId(item) == id) {
                return true;
            }
        }
        return false;
    }

    @FunctionalInterface
    private interface IdExtractor<T> {
        int getId(T item);
    }
}
