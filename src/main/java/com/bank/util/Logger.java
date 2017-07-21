package com.bank.util;


public class Logger {


    public Logger() {
        //TODO create file/open it
    }

    public static void log(LoggingLevel loggingLevel, String message){
        //TODO write to file
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement element = stackTraceElements[3];

        System.out.println(String.format("--%s [%s|%s] %s", loggingLevel.toString(), element.getClassName(), element.getMethodName(), message));
    }

    public static void warn(String message){
        log(LoggingLevel.WARN, message);
    }

    public static void info(String message){
        log(LoggingLevel.INFO, message);
    }

    public static void error(String message){
        log(LoggingLevel.ERROR, message);
    }



}
