package com.star.starboot.common.utils;

import ch.qos.logback.classic.gaffer.PropertyUtil;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.util.ByteSource;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;
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



    /**
     * 根据密码生成随机的盐和密文
     * @param password
     * @return
     */
    public static String[] encryptPassword(Object password){
        //生成盐值
        String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
        String cipherText = encryptPassword(password, salt);
        return new String[]{salt, cipherText};
    }

    /**
     * 根据密码和盐生成密文
     * @param password
     * @return
     */
    public static String encryptPassword(Object password, String salt){
        // 默认算法是SHA-512
        DefaultHashService hashService = new DefaultHashService();
        HashRequest request = new HashRequest.Builder().setAlgorithmName("MD5")
                .setSource(ByteSource.Util.bytes(password)).setSalt(ByteSource.Util.bytes(salt))
                .setIterations(2).build();
        return hashService.computeHash(request).toHex();
    }

    /**
     * 判断请求是否是ajax请求
     * @param request
     * @return
     */
    public static Boolean isAjax(HttpServletRequest request){
        if(request.getHeader("x-requested-with") != null
                && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @return
     */
    public static String parseTime(LocalDateTime localDateTime){
        //日期格式化，通用时间表达式
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        return fmt.print(localDateTime);
    }


    /**
     * 补0
     * @param size
     * @param value
     * @return
     */
    public static String fillZero(int size, int value) {
        return String.format("%0" + size + "d", value);
    }
}
