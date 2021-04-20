package com.star.starboot.config.aliyunoss;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.star.starboot.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.config.aliyunoss
 * @Description: 阿里云oss
 * @Author: xpy
 * @Date: Created in 2021年04月07日 3:13 下午
 */

/**
 * 阿里云oss工具类
 * bucket可设置为私有（读取和写操作需要有权限）、公共读（直接访问路径可读取）
 * 通过阿里云STS (Security Token Service) 进行临时授权访问
 * 需要在RAM访问控制中创建用户、角色、授权（AliyunOSSFullAccess、AliyunSTSAssumeRoleAccess）
 */
@Component
@EnableConfigurationProperties(AliYunOssConfig.class)
public class AliYunOssStsUtils {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public final static String OSS_ROOT_PATH = "root";//根目录

    @Autowired
    private AliYunOssConfig aliYunOssConfig;


    /**
     * 根据子账号访问权限获取阿里云oss sts临时权限（临时访问凭证），该凭证可保存到缓存中
     *
     * @param roleSessionName 临时Token的会话名称，RoleSessionName 是临时Token的会话名称，自己指定用于标识你的用户，主要用于审计，或者用于区分Token颁发给谁
     * @return 令牌
     * @throws ClientException
     */
    public AssumeRoleResponse getAssumeRole(String roleSessionName) throws Exception {

        try {
            // 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
            // 只有 RAM用户（子账号）才能调用 AssumeRole 接口
            // 阿里云主账号的AccessKeys不能用于发起AssumeRole请求
            // 请首先在RAM控制台创建一个RAM用户，并为这个用户创建AccessKeys
            IClientProfile profile = DefaultProfile.getProfile("", aliYunOssConfig.getAccessKeyId(), aliYunOssConfig.getAccessKeySecret());
            DefaultAcsClient client = new DefaultAcsClient(profile);
            // 创建一个 AssumeRoleRequest 并设置请求参数
            final AssumeRoleRequest request = new AssumeRoleRequest();
//        request.setVersion(aliyunOssSTS_API_VERSION);
            request.setMethod(MethodType.POST);
            // 此处必须为 HTTPS
            request.setProtocol(ProtocolType.HTTPS);

            // RoleSessionName 是临时Token的会话名称，自己指定用于标识你的用户，主要用于审计，或者用于区分Token颁发给谁
            // 但是注意RoleSessionName的长度和规则，不要有空格，只能有'-' '_' 字母和数字等字符
            // 具体规则请参考API文档中的格式要求
            // 临时Token的会话名称，自己指定用于标识你的用户，主要用于区分Token颁发给谁
            // acs:ram::$accountID:role/$roleName
            request.setRoleSessionName(roleSessionName);
            // RoleArn 需要在 RAM 控制台上获取
            request.setRoleArn(aliYunOssConfig.getRoleArn());

            // 授权策略
            //request.setPolicy(readJson(aliyunOssPolicyFile));
            // 设置token时间（最小15分钟，最大1小时） The Min/Max value of DurationSeconds is 15min/1hr
            //request.setDurationSeconds(60 * 60L);
            // 发起请求，并得到response

            final AssumeRoleResponse assumeRoleResponse = client.getAcsResponse(request);
            System.out.println("临时账号ID getAccessKeyId()=============" + assumeRoleResponse.getCredentials().getAccessKeyId());
            System.out.println("临时密码 getAccessKeySecret()=============" + assumeRoleResponse.getCredentials().getAccessKeySecret());
            System.out.println("临时token getSecurityToken()=============" + assumeRoleResponse.getCredentials().getSecurityToken());
            System.out.println("有效时间 getExpiration()=============" + assumeRoleResponse.getCredentials().getExpiration());

            logger.info("assumeRoleResponse ======\n{}", JSON.toJSONString(assumeRoleResponse));

            return assumeRoleResponse;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取ossClient客户端
     *
     * @param roleSessionName 临时Token的会话名称，RoleSessionName 是临时Token的会话名称，自己指定用于标识你的用户，主要用于审计，或者用于区分Token颁发给谁
     * @return
     */
    public OSSClient getOssClient(String roleSessionName) {
        try {
            AssumeRoleResponse assumeRoleResponse = this.getAssumeRole(roleSessionName);
            // 用户拿到STS临时凭证后，通过其中的安全令牌（SecurityToken）和临时访问密钥（AccessKeyId和AccessKeySecret）生成OSSClient。
            // 创建OSSClient实例。

            return new OSSClient(aliYunOssConfig.getOssEndPoint(), new DefaultCredentialProvider(assumeRoleResponse.getCredentials().getAccessKeyId(), assumeRoleResponse.getCredentials().getAccessKeySecret(), assumeRoleResponse.getCredentials().getSecurityToken()), null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取文件资源url（签名）
     *
     * @param ossFilePath
     * @param roleSessionName
     * @return 返回经过签名的资源url
     */
    public String getOssSignUrl(String ossFilePath, String roleSessionName) {
        OSSClient ossClient = null;
        try {
            ossClient = this.getOssClient(roleSessionName);
            // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
            return this.getOssSignUrl(ossClient, aliYunOssConfig.getBucket(), ossFilePath).toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                // 关闭OSSClient。
                ossClient.shutdown();
            }
        }
        return "";
    }


    /**
     * 获取文件资源url（签名）
     *
     * @param bucketName
     * @param ossFilePath
     * @param roleSessionName
     * @return 返回经过签名的资源url
     */
    public String getOssSignUrl(String bucketName, String ossFilePath, String roleSessionName) {
        OSSClient ossClient = null;
        try {
            ossClient = this.getOssClient(roleSessionName);
            // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
            return this.getOssSignUrl(ossClient, bucketName, ossFilePath).toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                // 关闭OSSClient。
                ossClient.shutdown();
            }
        }
        return "";
    }

    /**
     * 返回oss 签名URL
     *
     * @param ossClient
     * @param bucketName
     * @param ossFilePath
     * @return
     */
    public URL getOssSignUrl(OSSClient ossClient, String bucketName, String ossFilePath) {
        //默认1小时
        int urlExpirationMinute = 60;
        if (aliYunOssConfig.getUrlExpirationMinute() != null && aliYunOssConfig.getUrlExpirationMinute() > 0) {
            urlExpirationMinute = aliYunOssConfig.getUrlExpirationMinute();
        }
        // 设置URL过期时间为1小时。
        Date expiration = new Date(new Date().getTime() + (urlExpirationMinute * 60 * 1000));
        // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
        return ossClient.generatePresignedUrl(bucketName, ossFilePath, expiration);
    }


    /**
     * 上传文件
     *
     * @param roleSessionName
     * @param sourceFile
     * @param ossFilePath
     * @return
     */
    public String uploadObjectToOss(String roleSessionName, String ossFilePath, File sourceFile) {
        String url = "";
        FileInputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(sourceFile);
            outputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int temp;
            while ((temp = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, temp);
            }
            url = this.uploadObjectToOss(roleSessionName, ossFilePath, outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return url;
    }

    /**
     * 上传文件
     *
     * @param roleSessionName 临时会话名称，用于标识用户token
     * @param ossFilePath
     * @param bytes
     * @return
     */
    public String uploadObjectToOss(String roleSessionName, String ossFilePath, byte[] bytes) {
        OSSClient ossClient = null;
        try {
            ossClient = this.getOssClient(roleSessionName);

            //判断bucketName是否存储，如果不存在则创建
            boolean isExist = ossClient.doesBucketExist(aliYunOssConfig.getBucket());
            if (!isExist) {
                ossClient.createBucket(aliYunOssConfig.getBucket());
            }
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ossClient.putObject(aliYunOssConfig.getBucket(), ossFilePath, byteArrayInputStream, null);

            String url = this.getOssSignUrl(ossClient, aliYunOssConfig.getBucket(), ossFilePath).toString();
            logger.info("域名和文件路径且签名加密:\n{}", url);

            // "http://你的BucketName.你的Endpoint/自定义路径/" + fileName;
            String fileDomainUrl = "http://".concat(aliYunOssConfig.getBucket()).concat(".").concat(aliYunOssConfig.getOssEndPoint());
            String fileUrl = fileDomainUrl.concat("/").concat(ossFilePath);
            logger.info("域名路径:\n{}", fileDomainUrl);
            logger.info("域名和文件路径:\n{}", fileUrl);
            logger.info("文件路径:\n{}", ossFilePath);
            return fileUrl;
        } catch (Exception e) {
            e.printStackTrace();
            throw  new BusinessException("上传失败");
        } finally {
            this.closeOssClient(ossClient);
        }
    }


    /**
     * 关闭客户端
     *
     * @param ossClient
     */
    public void closeOssClient(OSSClient ossClient) {
        if (ossClient != null) {
            ossClient.shutdown();
        }
    }


    /**
     * 删除某个Object
     *
     * @param roleSessionName
     * @param bucketUrl
     * @return
     */
    public boolean deleteObject(String roleSessionName, String bucketUrl) {
        OSSClient client = null;
        try {
            client = this.getOssClient(roleSessionName);
            // 删除Object.
            client.deleteObject(aliYunOssConfig.getBucket(), bucketUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeOssClient(client);
        }
        return true;

    }

    /**
     * 删除多个Object
     *
     * @param roleSessionName
     * @param bucketUrls
     * @return
     */
    public boolean deleteObjects(String roleSessionName, List<String> bucketUrls) {
        OSSClient client = null;
        try {
            client = this.getOssClient(roleSessionName);
            // 删除Object.
            DeleteObjectsResult deleteObjectsResult = client.deleteObjects(new DeleteObjectsRequest(aliYunOssConfig.getBucket()).withKeys(bucketUrls));
            List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            client.shutdown();
        }
        return true;

    }
}
