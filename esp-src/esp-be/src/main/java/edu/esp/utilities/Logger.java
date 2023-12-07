package edu.esp.utilities;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    private static final DateTimeFormatter formatter
            = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    /**
     * Logs a message from a specific class with a specified type (color).
     *
     * @param className The name of the class invoking the logging.
     * @param msg       The message to be logged.
     * @param type      The type of message (0 - green for a success, 1 - red for failure, default for other).
     */
    public static void logMsgFrom (String className, String msg, int type) {
        String modifiedMessage =
                className + "(" +LocalTime.now().format(formatter) + ")"
                        + ": " + msg;
        log(modifiedMessage, type);
    }

    /**
     * Prints the message with appropriate color based on the type.
     *
     * @param msg  The message to be printed.
     * @param type The type of message (0 - green for a success, 1 - red for failure, default for other).
     */
    private static void log (String msg, int type) {
        switch (type) {
            case 0: // Success or acceptance, use green
                System.out.println(ANSI_GREEN + msg + ANSI_RESET);
                break;
            case 1:
                System.out.println(ANSI_RED + msg + ANSI_RESET);
                break;
            default:
                System.out.println(msg);
        }
    }

}
