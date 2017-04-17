package io.home.assignment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;

public final class Utils {
    private Utils() {
    }
    public static Logger getLogger(Class clazz) {
        return NOPLogger.NOP_LOGGER;
        //return LoggerFactory.getLogger(clazz);
    }
}
