package edu.marmara.annotator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
    private static Log logger = new Log();

    private Log() {
    }

    public static Log getInstance() {
        return logger;
    }

    private String getTime() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    private String createMessage(String className, String time, String message) {
        String logMessage = String.format("%s [%s] INFO %s", time, className, message);
        System.out.println(logMessage);
        return logMessage;
    }

    private void writeMessageToFile(String logMessage) {
        try (FileWriter f = new FileWriter("app.log", true);
             BufferedWriter b = new BufferedWriter(f);
             PrintWriter p = new PrintWriter(b)) {
            p.println(logMessage);

        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void log(String message) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement element = stackTrace[2];
        String[] classNameFull = element.getClassName().split("\\.");
        String className = classNameFull[classNameFull.length - 1];

        String time = getTime();

        String logMessage = createMessage(className, time, message);
        writeMessageToFile(logMessage);
    }
}
