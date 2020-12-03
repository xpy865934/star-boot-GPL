package com.star.starboot.config.shiro;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.config.shiro
 * @Description: 登录类型
 * @Author: xpy
 * @Date: Created in 2019年06月11日 下午7:14
 */
public enum LoginType {
    /**
     * 通用
     */
    COMMON("common_realm"),
    /**
     * 用户密码登录
     */
    USER_PASSWORD("user_password_realm"),
    /**
     * 手机号码密码登录
     */
    USER_PHONE("user_phone_realm"),
    /**
     * 第三方登录(微信登录)
     */
    WECHAT_LOGIN("wechat_login_realm");

    private String type;

    private LoginType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return this.type.toString();
    }
}
