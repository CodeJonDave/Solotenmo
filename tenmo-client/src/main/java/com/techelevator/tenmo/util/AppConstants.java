package com.techelevator.tenmo.util;

public class AppConstants {
    public static final int TRANSFER_MIN = 3001;
    public static final int USER_MIN = 1001;

    public static final String DIRECTORY_NAME = "tenmo-client";
    public static final String INFO_LOG_FILENAME = "info.log";
    public static final String WARN_LOG_FILENAME = "warn.log";
    public static final String ERROR_LOG_FILENAME = "error.log";


    public enum Type {
        SEND("Send"),
        REQUEST("Request");

        private final String displayValue;

        Type(String displayValue) {
            this.displayValue = displayValue;
        }

        @Override
        public String toString() {
            return displayValue;
        }
    }

    public enum Status {
        APPROVED("Approved"),
        PENDING("Pending"),
        REJECTED("Rejected");

        private final String displayValue;

        Status(String displayValue) {
            this.displayValue = displayValue;
        }

        @Override
        public String toString() {
            return displayValue;
        }
    }

    public enum ListType {
        FULL("Full"),
        PENDING("Pending");

        private final String displayValue;

        ListType(String displayValue) {
            this.displayValue = displayValue;
        }

        @Override
        public String toString() {
            return displayValue;
        }
    }
}
