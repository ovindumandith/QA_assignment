package com.orangehrm.qa.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for reading configuration values from {@code config.properties}.
 *
 * <p>Properties are loaded once in a static initialiser from the test classpath.
 * Call {@link #get(String)} for string values and {@link #getInt(String)} for
 * integer values throughout the framework.
 */
public class ConfigReader {

    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "config.properties";

    static {
        try (InputStream stream = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {

            if (stream == null) {
                throw new RuntimeException(
                        "Configuration file not found on classpath: " + CONFIG_FILE
                                + ". Ensure it exists under src/test/resources/.");
            }
            properties.load(stream);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to load configuration file: " + CONFIG_FILE, e);
        }
    }

    /** Private constructor — all members are static; instantiation is not intended. */
    private ConfigReader() {}

    /**
     * Returns the string value associated with the given property key.
     *
     * @param key the property key (e.g., {@code "base.url"})
     * @return the trimmed string value, or {@code null} if the key is absent
     */
    public static String get(String key) {
        String value = properties.getProperty(key);
        return value != null ? value.trim() : null;
    }

    /**
     * Returns the integer value associated with the given property key.
     *
     * @param key the property key (e.g., {@code "implicit.wait"})
     * @return the parsed integer value
     * @throws RuntimeException if the key is absent or the value is not a valid integer
     */
    public static int getInt(String key) {
        String value = get(key);
        if (value == null) {
            throw new RuntimeException(
                    "Property key not found in " + CONFIG_FILE + ": " + key);
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException(
                    "Property '" + key + "' cannot be parsed as an integer. Value: '" + value + "'", e);
        }
    }
}
