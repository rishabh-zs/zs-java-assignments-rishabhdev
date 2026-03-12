package com.zs.assignment12.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for fetching standard SLF4J Loggers.
 */
public class LoggerUtil {
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}