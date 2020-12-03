package com.star.starboot.config.shiro;

import com.star.starboot.constant.SystemConstant;
import com.star.starboot.system.dto.UsersDto;
import com.star.starboot.system.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.config.shiro
 * @Description: 用户密码登录Realm
 * @Author: xpy
 * @Date: Created in 2019年06月11日 下午7:26
 */
@Slf4j
public class UserPhoneRealm extends AuthorizingRealm {

    @Autowired
    private UsersService usersService;

    @Override
    public String getName() {
        return LoginType.USER_PHONE.getType();
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        if (token instanceof UserToken) {
            return ((UserToken) token).getLoginType() == LoginType.USER_PHONE;
        } else {
            return false;
        }
    }

    @Override
    public void setAuthorizationCacheName(String authorizationCacheName) {
        super.setAuthorizationCacheName(authorizationCacheName);
    }

    @Override
    protected void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected SimpleAuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 认证信息.(身份验证) : Authentication 是用来验证用户身份
     *
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("---------------- 手机号密码登录 ----------------------");
        UserToken token = (UserToken) authenticationToken;
        // 用户手机号
        String tel = token.getUsername();
        // 用户选择的公司
        String companyCode = token.getCompanyCode();

        // 从数据库获取对应手机号密码的用户
        UsersDto user = usersService.getUserByUserTelAndCompanyCode(tel,companyCode);
        if (user != null) {
            // 用户为禁用状态,1为禁用状态
            if (SystemConstant.USERFORBID.equals(user.getWorking().toString())) {
                throw new DisabledAccountException("账户已被禁用，请联系管理员！");
            }
            SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                    user, //用户
                    user.getPassword(), //密码
                    getName()  //realm name
            );
            return authenticationInfo;
        }
        throw new UnknownAccountException("手机号不存在");
    }
}
