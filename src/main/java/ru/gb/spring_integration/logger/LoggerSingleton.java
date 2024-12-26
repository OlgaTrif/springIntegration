package ru.gb.spring_integration.logger;

/**
 * В этом примере предполагается, что мы хотим использовать один и тот же логгер во всём приложении
 * */

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class LoggerSingleton {

    private static Logger logger;

    private LoggerSingleton() {
        try {
            FileHandler fileHandler = new FileHandler("logfile.log");
            logger = Logger.getLogger("MyLogger");
            logger.setUseParentHandlers(false);
            logger.addHandler(fileHandler);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getInstance() {
        if (logger == null) {
            new LoggerSingleton();
        }
        return logger;
    }
}
