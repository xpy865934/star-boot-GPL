package com.star.starboot.common.utils;

import ch.qos.logback.classic.gaffer.PropertyUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.qcnt.qcnt.utils
 * @Description: ${Description}
 * @Author: xpy
 * @Date: Created in 2020年02月23日 11:38
 */
public class CommonUtils {

    private static Properties loadPropertiesByFileName(String fileName) {
        Properties prop = new Properties();
        InputStream inputStream = PropertyUtil.class.getClassLoader().getResourceAsStream(fileName);

        try {
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prop;

    }

    /**
     * 是否是线上环境
     */
    public static boolean isProduction() {
        Properties properties = loadPropertiesByFileName("application.yml");
        return properties.get("active").toString().equals("prod");
    }

    public static boolean isProd() {
        return isProduction();
    }

    public static String get(String propertyName) {
        Properties properties = loadPropertiesByFileName("application.yml");
        String config = properties.getProperty("spring.profiles.active");
        String fileName = "application-" + config + ".yml";
        return loadPropertiesByFileName(fileName).get(propertyName).toString();
    }

    /**
     * 获取class下面的resources路径
     * @return
     */
    public static String getResourcesPath(){
        return Thread.currentThread().getContextClassLoader().getResource("").getPath()+"/";
    }
}
