package com.star.starboot.config.shiro;

import com.alibaba.druid.util.StringUtils;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.config.shiro
 * @Description: 自定义Session获取方式
 * 传统结构项目中，shiro从cookie中读取sessionId以此来维持会话，
 * 在前后端分离的项目中（也可在移动APP项目使用），我们选择在ajax的请求头中传递sessionId，
 * 因此需要重写shiro获取sessionId的方式。自定义MySessionManager类继承DefaultWebSessionManager类，重写getSessionId方法
 * @Author: xpy
 * @Date: Created in 2019年06月10日 下午8:22
 */
public class MySessionManager extends DefaultWebSessionManager {
    private static final String TOKEN = "token";

    private static final String REFERENCED_SESSION_ID_SOURCE = "token";

    public MySessionManager() {
        super();
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        //获取请求头，或者请求参数中的Token
//        String id = StringUtils.isEmpty(WebUtils.toHttp(request).getHeader(AUTHORIZATION))
//                ? request.getParameter(AUTHORIZATION) : WebUtils.toHttp(request).getHeader(AUTHORIZATION);

        String id = WebUtils.toHttp(request).getHeader(TOKEN);
        //如果请求头中有 token 则其值为sessionId
        if (!StringUtils.isEmpty(id)) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return id;
        } else {
            //否则按默认规则从cookie取sessionId
            return super.getSessionId(request, response);
        }
    }
}
