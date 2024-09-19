package com.techelevator.tenmo.util;


public class BasicLoggerException extends RuntimeException {

    public BasicLoggerException(String message, Exception e) {
        super(message);
    }
}
