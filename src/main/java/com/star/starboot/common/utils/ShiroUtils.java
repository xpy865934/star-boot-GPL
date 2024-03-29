package com.star.starboot.common.utils;

import com.star.starboot.system.dto.UsersDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

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
        String sessionId=null;
        sessionId = request.getHeader("token");
        // 如果获取头部信息出错，例如img src，则获取请求的参数
        if(StringUtils.isEmpty(sessionId)){
            sessionId = request.getParameter("token");
        }
        // 判断是否是ajax,如果是，则获取token,如果不是，则获取FLOWABLE_REMEMBER_ME
//        if(CommonUtils.isAjax(request)){
//           sessionId = request.getHeader("token");
//        } else {
//            // 支持flowable，加入flowable cookie
//            sessionId = request.getHeader("FLOWABLE_REMEMBER_ME");
//        }
        SessionKey key = new WebSessionKey(sessionId,request,response);
        try{
            Session se = SecurityUtils.getSecurityManager().getSession(key);
            Object obj = se.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            SimpleAuthorizationInfo info = (SimpleAuthorizationInfo)se.getAttribute("permissions");
            SimplePrincipalCollection coll = (SimplePrincipalCollection) obj;
            UsersDto userDto = (UsersDto)coll.getPrimaryPrincipal();
            if(!StringUtils.isEmpty(info)){
                // 第一次登陆时需要写入日志，此时还获取不到日志信息
                userDto.setRoles(info.getRoles());
                userDto.setPermissions(info.getStringPermissions());
            }
            return userDto;
        }catch(Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
//            throw new BusinessException(ResultCode.USER_INFO_LOSE_ERROR.getMsg());
        }finally{
        }
        return null;
    }

    /**
     * 检查当前用户是否具有该权限
     * @param permission
     * @return
     */
    public Boolean checkPermission(String permission){
        SimpleAuthorizationInfo info = (SimpleAuthorizationInfo) SecurityUtils.getSubject().getSession().getAttribute("permissions");

        Set<String> stringPermissions = info.getStringPermissions();
        for (String str : stringPermissions) {
            if(permission.equals(str)){
                return true;
            }
        }
        return false;
    }
}
