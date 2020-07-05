package com.star.starboot.config.shiro;

import lombok.Data;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.qcnt.qcnt.system.shiro
 * @Description: 自定义登录身份
 * @Author: xpy
 * @Date: Created in 2019年06月11日 下午7:24
 */
@Data
public class UserToken extends UsernamePasswordToken {
    //登录方式
    private LoginType loginType;
    // 公司id
    private String companyCode;

    // uni push
    private String clientId;
    private String appId;
    private String appkey;

    // TODO 由于是demo方法，此处微信只传一个code参数，其他参数根据实际情况添加


    public UserToken(LoginType loginType, final String userCode, final String password, String companyCode, String clientId, String appId, String appkey) {
        super(userCode, password);
        this.loginType = loginType;
        this.companyCode = companyCode;
        this.clientId = clientId;
        this.appId = appId;
        this.appkey = appkey;
    }
}
