package com.techelevator.tenmo.util;



import com.techelevator.tenmo.exception.BasicLoggerException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BasicLogger {

    private static PrintWriter infoPw = null;
    private static PrintWriter warnPw = null;
    private static PrintWriter errorPw = null;

    private static final String DIRECTORY_NAME = "tenmo-server";
    private static final String INFO_LOG_FILENAME = "info.log";
    private static final String WARN_LOG_FILENAME = "warn.log";
    private static final String ERROR_LOG_FILENAME = "error.log";

    static {
        try {
            infoPw = getPrintWriter(INFO_LOG_FILENAME);
            warnPw = getPrintWriter(WARN_LOG_FILENAME);
            errorPw = getPrintWriter(ERROR_LOG_FILENAME);
        } catch (FileNotFoundException e) {
            throw new BasicLoggerException("Unable to initialize log files", e);
        }
    }

    public static void info(String message) {
        log(message, infoPw);
    }

    public static void warn(String message) {
        log(message, warnPw);
    }

    public static void error(String message, Exception ex) {
        log(message + ex.getMessage(), errorPw);
    }

    private static void log(String message, PrintWriter pw) {
        if (pw != null) {
            pw.println(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + " " + message);
            pw.flush();
        }
    }

    private static PrintWriter getPrintWriter(String logFileName) throws FileNotFoundException {
        String userDir = System.getProperty("user.dir");

        if (!userDir.endsWith(DIRECTORY_NAME)) {
            userDir += File.separator + DIRECTORY_NAME;
        }
        File logDir = new File(userDir + File.separator + "logs");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
        File logFile = new File(logDir, LocalDate.now().format(DateTimeFormatter.ISO_DATE) + "-" + logFileName);
        return new PrintWriter(new FileOutputStream(logFile, true));
    }


}
