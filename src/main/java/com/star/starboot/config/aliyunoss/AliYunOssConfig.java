package com.star.starboot.config.aliyunoss;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.config.aliyunoss
 * @Description: 阿里云oss配置
 * @Author: xpy
 * @Date: Created in 2021年04月01日 4:40 下午
 */
@ConfigurationProperties(prefix = "aliyun")
public class AliYunOssConfig {

    private String bucket; //oss bucket
    private String ossEndPoint; //oss endpoint
    private String accessKeyId; //账号accessKeyId
    private String accessKeySecret; //账号accessKeySecret
    private String roleArn; //角色ram
    private Integer urlExpirationMinute; //url过期时间（分钟）

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getOssEndPoint() {
        return ossEndPoint;
    }

    public void setOssEndPoint(String ossEndPoint) {
        this.ossEndPoint = ossEndPoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getRoleArn() {
        return roleArn;
    }

    public void setRoleArn(String roleArn) {
        this.roleArn = roleArn;
    }

    public Integer getUrlExpirationMinute() {
        return urlExpirationMinute;
    }

    public void setUrlExpirationMinute(Integer urlExpirationMinute) {
        this.urlExpirationMinute = urlExpirationMinute;
    }
}
