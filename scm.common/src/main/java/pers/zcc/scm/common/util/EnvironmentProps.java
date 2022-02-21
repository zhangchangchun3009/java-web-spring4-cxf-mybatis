package pers.zcc.scm.common.util;

import java.io.FileInputStream;
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

}
