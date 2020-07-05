package com.star.starboot.config.shiro;

import com.alibaba.fastjson.JSONObject;
import com.star.starboot.common.enums.ResultCode;
import com.star.starboot.common.utils.ShiroUtils;
import com.star.starboot.common.vo.Result;
import com.star.starboot.system.dto.UsersDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.qcnt.qcnt.system.shiro
 * @Description: 重写shiro过滤器
 * @Author: xpy
 * @Date: Created in 2019年06月22日 23:37
 */
@Slf4j
public class ShiroFormAuthenticationFilter extends FormAuthenticationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //Always return true if the request's method is OPTIONSif (request instanceof HttpServletRequest) {
        if (((HttpServletRequest) request).getMethod().toUpperCase().equals("OPTIONS")) {
            return true;
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                log.error("Login submission detected.  Attempting to execute login.");
                return executeLogin(request, response);
            } else {
                log.trace("Login page view.");
                return true;
            }
        } else {
            // 判断登录信息是否过期
            UsersDto userInfo = ShiroUtils.build().getUserInfo();
            if(StringUtils.isEmpty(userInfo)){
                HttpServletRequest req = (HttpServletRequest) request;
                HttpServletResponse resp = (HttpServletResponse) response;
                if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
                    resp.setStatus(HttpStatus.OK.value());
                    return true;
                }
                log.error("用户未登录");
                //前端Ajax请求，则不会重定向
                resp.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
                resp.setHeader("Access-Control-Allow-Credentials", "true");
                resp.setContentType("application/json; charset=utf-8");
                resp.setCharacterEncoding("UTF-8");
                PrintWriter out = resp.getWriter();
                Result result = Result.create(ResultCode.USER_INFO_LOSE_ERROR);
                out.println(JSONObject.toJSON(result));
                out.flush();
                out.close();
                return false;
            }
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;
            if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
                resp.setStatus(HttpStatus.OK.value());
                return true;
            }
            log.error("Attempting to access a path which requires authentication.  Forwarding to the " +
                    "Authentication url [" + getLoginUrl() + "]");
            //前端Ajax请求，则不会重定向
            resp.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
            resp.setHeader("Access-Control-Allow-Credentials", "true");
            resp.setContentType("application/json; charset=utf-8");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter out = resp.getWriter();
            Result result = Result.create(ResultCode.UNAUTHO_ERROR);
            out.println(JSONObject.toJSON(result));
            out.flush();
            out.close();
            return false;
        }
    }
}
