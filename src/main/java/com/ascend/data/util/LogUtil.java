package com.ascend.data.util;

/**
 * Created by thinhly on 3/8/16.
 */

import org.apache.log4j.BasicConfigurator;
import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.LogManager;

import java.io.File;

public class LogUtil {
    private static final Logger log = LogManager.getLogger(LogUtil.class);
    private static LogUtil instance = null;

    private LogUtil() {
    }

    public static LogUtil getInstance() {
        if(instance == null){
            instance = new LogUtil();
            // Set up a simple configuration that logs on the console.
            BasicConfigurator.configure();
        }
        return instance;
    }

    public void logInfo(Class name, String message) {
        String content = "[INFO] " + name.getSimpleName() + ": " + message;
        log.info(content);
    }

    public void logDebug(Class name, String message) {
        String content = "[DEBUG] " + name.getSimpleName() + ": " + message;
        log.debug(content);
    }

    public void logError(Class name, String message) {
        String content = "[ERROR] " + name.getSimpleName() + ": " + message;
        log.error(content);
    }
}
