package com.bank.util.Logging;


import com.bank.projection.logging.LogEntryProjection;
import com.bank.service.time.TimeService;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Logger {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private static final String LOG_NAME = "log.log";
    private static final String LOG_DIRECTORY = System.getProperty("user.dir") + File.separator + LOG_NAME;

    private static PrintWriter printWriter;
    private static File logFile = new File(LOG_DIRECTORY);

    static {
        try {
            FileWriter fileWriter = new FileWriter(logFile, true);
            printWriter = new PrintWriter(new BufferedWriter(fileWriter));
            Logger.info("Logger initialized");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Logger() {
//        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        File logFile = new File(LOG_DIRECTORY);
        try {
            FileWriter fileWriter = new FileWriter(logFile);
            printWriter = new PrintWriter(new BufferedWriter(fileWriter));
            Logger.info("Logger initialized");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void log(LoggingLevel loggingLevel, String message, Object... objects) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement element = stackTraceElements[3];

        String logString = String.format("{%s} %-6s [%50s|%20s] %s", simpleDateFormat.format(TimeService.TIMESIMULATOR.getCurrentDate()), loggingLevel.toString(), element.getClassName(), element.getMethodName(), String.format(message, objects));

        System.out.println(logString);
        printWriter.println(logString);
        printWriter.flush();
    }

    public static List<LogEntryProjection> getLogsBetween(Date a, Date b) {
        List<LogEntryProjection> list = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(logFile))) {
            for (String line; (line = br.readLine()) != null; ) {
                int endDateIndex = line.indexOf("}");
                String dateString = line.substring(line.indexOf("{") + 1, endDateIndex);
                try {
                    Date dateEntry = simpleDateFormat.parse(dateString);
                    if (dateEntry.after(a)) {
                        if (dateEntry.before(b)) {
                            String eventDetails = line.substring(endDateIndex + 1);
                            LogEntryProjection logEntryProjection = new LogEntryProjection(dateString, eventDetails);
                            list.add(logEntryProjection);
                        } else {
                            break;
                        }
                    }
                } catch (ParseException e) {
                    throw new IllegalStateException("Log entry could not be parsed. Entry="+line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void warn(String message, Object... objects) {
        log(LoggingLevel.WARN, message, objects);
    }

    public static void info(String message, Object... objects) {
        log(LoggingLevel.INFO, message, objects);
    }

    public static void error(String message, Object... objects) {
        log(LoggingLevel.ERROR, message, objects);
    }


}
