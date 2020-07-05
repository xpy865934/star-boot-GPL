package com.star.starboot.common.utils;

import com.star.starboot.system.dto.UsersDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.common.utils
 * @Description: Shiro工具类
 * @Author: xpy
 * @Date: Created in 2019年08月11日 16:41
 */
@Slf4j
public class ShiroUtils {

    private HttpServletRequest request;

    private HttpServletResponse response;

    public ShiroUtils(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
    }

    public static ShiroUtils  build(){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        return new ShiroUtils(request,response);
    }

    /**
     * 验证是否登陆
     */
    public boolean isAuthenticated(String sessionID, HttpServletRequest request, HttpServletResponse response){
        boolean status = false;

        SessionKey key = new WebSessionKey(sessionID,request,response);
        try{
            Session se = SecurityUtils.getSecurityManager().getSession(key);
            Object obj = se.getAttribute(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY);
            if(obj != null){
                status = (Boolean) obj;
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            Session se = null;
            Object obj = null;
        }

        return status;
    }
    /**
     * 获取用户登录信息
     */
    public UsersDto getUserInfo(){
        String sessionId = request.getHeader("token");
        boolean status = false;
        SessionKey key = new WebSessionKey(sessionId,request,response);
        try{
            Session se = SecurityUtils.getSecurityManager().getSession(key);
            Object obj = se.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            SimplePrincipalCollection coll = (SimplePrincipalCollection) obj;
            return (UsersDto)coll.getPrimaryPrincipal();
        }catch(Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }finally{
        }
        return null;
    }
}
