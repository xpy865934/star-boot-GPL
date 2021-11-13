package com.star.starboot.common.utils;

import ch.qos.logback.classic.gaffer.PropertyUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.util.ByteSource;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Properties;
import java.util.Random;

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

    /**
     * 产生4位随机数(0000-9999)
     *
     * @return 4位随机数
     */
    public static String getFourRandom() {
        return StringUtils.leftPad(new Random().nextInt(10000) + "", 4, "0");
    }

    /**
     * 获取一定长度的随机字符串
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


    /**
     * 文件转换成MultipartFile
     * @param picPath
     * @return
     */
    private static MultipartFile getMulFileByPath(String picPath) {
        FileItem fileItem = createFileItem(picPath);
        MultipartFile mfile = new CommonsMultipartFile(fileItem);
        return mfile;
    }


    /**
     * 文件转换成MultipartFile
     * @param fileInputStream
     * @param extFile  文件后缀名
     * @return
     */
    private static MultipartFile getMulFileByFileInputStream(FileInputStream fileInputStream, String extFile) {
        FileItem fileItem = createFileItem(fileInputStream, extFile);
        MultipartFile mfile = new CommonsMultipartFile(fileItem);
        return mfile;
    }

    private static FileItem createFileItem(String filePath)
    {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        String textFieldName = "textField";
        int num = filePath.lastIndexOf(".");
        String extFile = filePath.substring(num);
        FileItem item = factory.createItem(textFieldName, "text/plain", true,
                "thumb." + extFile);
        File newfile = new File(filePath);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try
        {
            FileInputStream fis = new FileInputStream(newfile);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192))
                    != -1)
            {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return item;
    }

    private static FileItem createFileItem(FileInputStream fileInputStream, String extFile)
    {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        String textFieldName = "textField";
        FileItem item = factory.createItem(textFieldName, "text/plain", true,
                "thumb." + extFile);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try
        {
            FileInputStream fis = fileInputStream;
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192))
                    != -1)
            {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return item;
    }


    /**
     * 将InputStream写入本地文件
     * @param destination 写入本地目录
     * @param input	输入流
     * @throws IOException
     */
    public static void writeToLocal(String destination, InputStream input) throws IOException {
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream(destination);
        while ((index = input.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        downloadFile.close();
        input.close();
    }

}
