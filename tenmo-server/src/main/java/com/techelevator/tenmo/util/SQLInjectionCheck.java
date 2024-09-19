package com.techelevator.tenmo.util;

public class SQLInjectionCheck {
    private static final String[] SQL_RISK_CHARACTERS = {
            "SELECT", "INSERT", "UPDATE", "DELETE", "DROP", "ALTER", "UNION",
            "EXEC", "TRUNCATE", "MERGE", "CALL", "CREATE", "GRANT", "REVOKE", "NULL",
            "[", ";", "'", "\"", "--", "]"," "
    };

    public static boolean isSafe(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        String upperCaseInput = input.toUpperCase();

        for (String risk : SQL_RISK_CHARACTERS) {
            if (upperCaseInput.contains(risk)) {
                return false;
            }
        }
        return true;
    }
}
