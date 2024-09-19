package com.techelevator.tenmo.exception;

import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.util.BasicLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public void handleGeneralExceptions(Exception ex) {
        BasicLogger.error("Unhandled exception: " + ex.getMessage(), ex);
    }

    @ExceptionHandler(DaoException.class)
    public void handleDaoException(DaoException ex) {
        BasicLogger.error("Data Access Object Exception: " + ex.getMessage(), ex);
    }

    @ExceptionHandler(UserListServiceException.class)
    public ResponseEntity<List<User>> handleUserListServiceException(UserListServiceException ex) {
        BasicLogger.error("User List Service Exception: " + ex.getMessage(), ex);
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<User> handleUserServiceException(UserServiceException ex) {
        BasicLogger.error("User Service Exception: " + ex.getMessage(), ex);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(BalanceAccessException.class)
    public ResponseEntity<BigDecimal> handleBalanceAccessException(BalanceAccessException ex) {
        BasicLogger.error("Balance Service Exception: " + ex.getMessage(), ex);
        return new ResponseEntity<>(new BigDecimal(0), HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(BalanceUpdateException.class)
    public ResponseEntity<Integer> handleBalanceUpdateException(BalanceUpdateException ex) {
        BasicLogger.error("Balance Update Exception: " + ex.getMessage(), ex);
        return new ResponseEntity<>(0, HttpStatus.NOT_MODIFIED);
    }

    @ExceptionHandler(TransferListAccessException.class)
    public ResponseEntity<List<TransferDto>> handleTransferListAccessException(TransferListAccessException ex) {
        BasicLogger.error("Transfer List Access Exception: " + ex.getMessage(), ex);
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TransferAccessException.class)
    public ResponseEntity<TransferDto> handleTransferAccessException(TransferAccessException ex) {
        BasicLogger.error("Transfer Access Exception ", ex);
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TransferStatusIdAccessException.class)
    public ResponseEntity<String> handleTransferStatusIdAccessException(TransferStatusIdAccessException ex) {
        BasicLogger.error("Transfer Status Access Exception ", ex);
        return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransferStatusDescAccessException.class)
    public ResponseEntity<Integer> handleTransferStatusDescAccessException(TransferStatusDescAccessException ex) {
        BasicLogger.error("Transfer Status Access Exception ", ex);
        return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransferUpdateException.class)
    public ResponseEntity<Integer> handleTransferUpdateException(TransferUpdateException ex) {
        BasicLogger.error("Transfer Update Exception ", ex);
        return new ResponseEntity<>(0, HttpStatus.NOT_MODIFIED);
    }

    @ExceptionHandler(TransferTypeDescAccessException.class)
    public ResponseEntity<Integer> handleTransferTypeDescAccessException(TransferTypeDescAccessException ex) {
        BasicLogger.error("Transfer Type Access Exception ", ex);
        return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransferTypeIdAccessException.class)
    public ResponseEntity<String> handleTransferTypeDescAccessException(TransferTypeIdAccessException ex) {
        BasicLogger.error("Transfer Type Access Exception ", ex);
        return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountAccessException.class)
    public ResponseEntity<Integer> handleAccountAccessException(AccountAccessException ex){
        BasicLogger.error("Account Access Exception ", ex);
        return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
    }

}

