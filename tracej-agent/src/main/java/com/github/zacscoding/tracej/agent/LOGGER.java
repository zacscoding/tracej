package com.github.zacscoding.tracej.agent;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * LOGGER for agent
 *
 * @GitHub : https://github.com/zacscoding
 */
public class LOGGER {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyMMdd HH:mm:ss");
    private static PrintWriter pw;

    static {
        pw = new PrintWriter(System.out);
    }

    /**
     * Print given info message with default id
     */
    public static void info(Object message) {
        println(parse("tracej : INFO", message));
    }

    /**
     * Print given info message with id
     */
    public static void info(String id, Object message) {
        println(parse(id, message));
    }

    /**
     * Print given error message with default id
     * @param message
     */
    public static void error(Object message) {
        error(message, null);
    }

    /**
     * Print given error message with default id
     */
    public static void error(Object message, Throwable t) {
        error("tracej : ERROR", message, t);
    }

    /**
     * Print given error message with id, stack trace of error
     */
    public static void error(String id, Object message, Throwable t) {
        println(parse(id, message));
        if (t != null) {
            println(getStackTrace(t));
        }
    }

    private static void println(String message) {
        println(pw, message);
    }

    private static String parse(String id, Object message) {
        String messageVal = message == null ? "null" : message.toString();
        if (id == null) {
            id = "tracej";
        }

        return new StringBuilder(20 + id.length() + messageVal.length())
                .append(DATE_FORMAT.format(new Date()))
                .append(' ')
                .append('[').append(id).append(']').append(' ').append(messageVal)
                .toString();
    }

    private static void println(PrintWriter printWriter, String message) {
        try {
            if (pw != null) {
                printWriter.println(message);
                printWriter.flush();
            }
        } catch (Throwable t) {
            // ignore
        }
    }

    private static String getStackTrace(Throwable t) {
        final String separator = System.getProperty("line.separator");
        StringBuffer sb = new StringBuffer();
        sb.append(t + separator);
        StackTraceElement[] se = t.getStackTrace();
        if (se != null) {
            for (int i = 0; i < se.length; i++) {
                if (se[i] != null) {
                    sb.append("\t" + se[i]);
                    if (i != se.length - 1) {
                        sb.append(separator);
                    }
                }
            }
        }

        return sb.toString();
    }
}
