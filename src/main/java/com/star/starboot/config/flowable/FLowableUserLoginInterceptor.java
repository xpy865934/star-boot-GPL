package com.star.starboot.config.flowable;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class FLowableUserLoginInterceptor implements HandlerInterceptor {

    protected org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String username = (String) request.getServletContext().getAttribute("flowableAdmin");
        if (null != username) {//已登录
            return true;
        } else {//未登录
            //直接重定向到登录页面
            response.sendRedirect(request.getContextPath() + "/static/login/index.html");
            return false;
        }
    }
}
