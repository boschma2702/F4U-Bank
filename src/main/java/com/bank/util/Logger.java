package com.bank.util;


public class Logger {


    public Logger() {
        //TODO create file/open it
    }

    public static void log(LoggingLevel loggingLevel, String message, Object... objects){
        //TODO write to file
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement element = stackTraceElements[3];

        System.out.println(String.format("--%s [%s|%s] %s", loggingLevel.toString(), element.getClassName(), element.getMethodName(), String.format(message, objects)));
    }

    public static void warn(String message, Object... objects){
        log(LoggingLevel.WARN, message, objects);
    }

    public static void info(String message, Object... objects){
        log(LoggingLevel.INFO, message, objects);
    }

    public static void error(String message, Object... objects){
        log(LoggingLevel.ERROR, message, objects);
    }



}
