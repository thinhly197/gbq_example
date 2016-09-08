package com.ascend.data.util;

/**
 * Created by thinhly on 3/8/16.
 */
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

import java.io.File;

public class AutoReloadProperty {

    private static PropertiesConfiguration configuration = null;

    private static LogUtil log = LogUtil.getInstance();

    public static void setFile(final String configFile) {
        configuration.setFile(new File(configFile));
    }

    public static void setPropertiesFile(PropertiesConfiguration config) {
        if(configuration == null) {
            configuration = config;
            configuration.setReloadingStrategy(new FileChangedReloadingStrategy());
            configuration.setEncoding("UTF-8");
        }
    }

    public static String getProperty (final String key) {
        if(configuration != null) {
            if (configuration.getProperty(key) != null) {
                return (String) configuration.getProperty(key);
            }
            else {
                return "";
            }
        }
        return "";
    }

    public static void setProperty (final String key, String value) {
        if(configuration != null) {
            configuration.setProperty(key, value);
            try {
                configuration.save();
            } catch (ConfigurationException e) {
                e.printStackTrace();
            }
        }
    }
}