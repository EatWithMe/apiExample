package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    private static String CONFIGURATION_FILE = "/application.properties";
    private static final String KEY = "env";

    private static final Properties properties;

    static {
        properties = new Properties();
        if (System.getenv(KEY) != null)
            CONFIGURATION_FILE = "/" + System.getenv(KEY) + ".properties";
        else if (System.getProperty(KEY) != null)
            CONFIGURATION_FILE = "/" + System.getProperty(KEY) + ".properties";
        try (InputStream inputStream = Configuration.class.getResourceAsStream(CONFIGURATION_FILE)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file " + CONFIGURATION_FILE, e);
        }
    }

    public static String getConfigurationValue(String key) {
        if (System.getenv(key) != null) return System.getenv(key);
        else if (System.getProperty(key) != null) return System.getProperty(key);
        else return properties.getProperty(key);
    }

    public Configuration() {

    }

}

