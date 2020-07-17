package com.star.starboot.config.shiro;

import com.star.starboot.common.utils.CommonUtils;
import com.star.starboot.system.dto.UsersDto;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.util.ByteSource;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.config.shiro
 * @Description: 自定义密码校验器
 * @Author: xpy
 * @Date: Created in 2019年06月11日 上午11:51
 */
public class CredentialsMatcher extends SimpleCredentialsMatcher {

    /**
     * 自定义验证，根据用户工号和公司code查询出用户信息，用用户信息中的盐值进行加密和数据库密码比对
     * @param token
     * @param info
     * @return
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        // 获取用户输入的密码
        Object tokenCredentials = getCredentials(token);

        // 获取数据库中的用户  // 此处应该是shiro的装饰者模式，在shiroConfig中设置了realm
        UsersDto usersDto = info.getPrincipals().oneByType(UsersDto.class);

        // 获得数据库中的密码
        Object accountCredentials = getCredentials(info);

        String tokenPassword = CommonUtils.encryptPassword(tokenCredentials,usersDto.getSalt());

        // 进行密码的比对
        return super.equals(tokenPassword, accountCredentials);
    }
}
