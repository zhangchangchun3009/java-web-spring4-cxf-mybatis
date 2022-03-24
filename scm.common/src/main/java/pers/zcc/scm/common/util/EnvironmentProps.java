package pers.zcc.scm.common.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnvironmentProps {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnvironmentProps.class);

    private static Map<String, String> propCache = new HashMap<>(32);

    private static String activeProfile = System.getProperty("spring.profiles.active");

    private static String baseDir = EnvironmentProps.class.getClassLoader().getResource("").getPath();

    public static Properties getProperties(String path) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(path));
        } catch (Exception e) {
            LOGGER.error("loadAllProperties e,", e);
            return properties;
        }
        try {
            for (Entry<Object, Object> entry : properties.entrySet()) {
                propCache.put(path + "/" + (String) entry.getKey(), (String) entry.getValue());
            }
        } catch (Exception e) {
            LOGGER.error("propCache.put e,", e);
        }
        return properties;
    }

    public static String getProperty(String path, String key) {
        String value = propCache.get(path + "/" + key);
        if (value != null) {
            return value;
        }
        Properties props = getProperties(path);
        return props.getProperty(key);
    }

    public static String getApplicationProp(String key) {
        return getProperty(baseDir + "conf/" + activeProfile + "/application.properties", key);
    }

    /**
     * get value as int
     * @param path
     * @param key
     * @param defaultValue
     * @return
     * @throws NumberFormatException
     */
    public static int getInteger(String path, String key, int defaultValue) throws NumberFormatException {
        String value = getProperty(path, key);
        return (value != null && !"".equals(value)) ? Integer.parseInt(value) : defaultValue;
    }

    /**
     * get value as long
     * @param path
     * @param key
     * @param defaultValue
     * @return
     * @throws NumberFormatException
     */
    public static long getLong(String path, String key, long defaultValue) throws NumberFormatException {
        String value = getProperty(path, key);
        return (value != null && !"".equals(value)) ? Long.parseLong(value) : defaultValue;
    }

    /**
     * get value as string
     * @param path
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(String path, String key, String defaultValue) {
        String value = getProperty(path, key);
        return (value != null && !"".equals(value)) ? value : defaultValue;
    }

    /**
     * get as Properties
     * @param in
     * @return
     * @throws IOException
     */
    public static Properties getProperties(InputStream in) {
        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            LOGGER.error("loadAllProperties e,", e);
        }
        return properties;
    }

    /**
     * getInteger
     * @param prop
     * @param key
     * @param defaultValue
     * @return
     * @throws NumberFormatException
     */
    public static int getInteger(Properties prop, String key, int defaultValue) throws NumberFormatException {
        String value = prop.getProperty(key);
        return (value != null && !"".equals(value)) ? Integer.parseInt(value) : defaultValue;
    }

    /**
     * getLong
     * @param prop
     * @param key
     * @param defaultValue
     * @return
     * @throws NumberFormatException
     */
    public static long getLong(Properties prop, String key, long defaultValue) throws NumberFormatException {
        String value = prop.getProperty(key);
        return (value != null && !"".equals(value)) ? Long.parseLong(value) : defaultValue;
    }

    /**
     * getString
     * @param prop
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(Properties prop, String key, String defaultValue) {
        String value = prop.getProperty(key);
        return (value != null && !"".equals(value)) ? value : defaultValue;
    }

}
