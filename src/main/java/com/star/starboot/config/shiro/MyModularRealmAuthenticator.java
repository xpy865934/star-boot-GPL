package com.star.starboot.config.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.Collection;
import java.util.HashMap;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.config.shiro
 * @Description: 多Realm登陆
 * @Author: xpy
 * @Date: Created in 2020年12月03日 9:43 上午
 */
public class MyModularRealmAuthenticator extends ModularRealmAuthenticator {

    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 判断getRealms()是否返回为空
        assertRealmsConfigured();

        // 所有Realm
        Collection<Realm> realms = getRealms();
        // 登录类型对应的所有Realm
        HashMap<String, Realm> realmHashMap = new HashMap<>(realms.size());
        for (Realm realm : realms) {
            realmHashMap.put(realm.getName(), realm);
        }

        UserToken token = (UserToken) authenticationToken;
        // 登录类型
        LoginType loginType = token.getLoginType();

        if (realmHashMap.get(loginType.getType()) != null) {
            return doSingleRealmAuthentication(realmHashMap.get(loginType.getType()), token);
        } else {
            return doMultiRealmAuthentication(realms, token);
        }

    }
}
