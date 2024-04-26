package com.star.starboot.aop;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.star.starboot.common.utils.IpUtils;
import com.star.starboot.common.utils.ShiroUtils;
import com.star.starboot.system.dto.UsersDto;
import com.star.starboot.system.entity.SysLog;
import com.star.starboot.system.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.aop
 * @Description: ${Description}
 * @Author: xpy
 * @Date: Created in 2020年07月01日 10:45
 */
@Aspect
@Component
@Slf4j
public class SysLogAspect {
    @Autowired
    private SysLogService sysLogService;

    //Controller层切点
    @Pointcut("@annotation(com.star.starboot.annotation.SysLog)")
    public void controllerAspect() { }


    /**
     * @Description 前置通知  用于拦截Controller层记录用户的操作
     * @date 2019年7月15日11:47:21
     */

    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        //读取session中的用户
        UsersDto usersDto = ShiroUtils.build().getUserInfo();

        if(StringUtils.isEmpty(usersDto)){
            String ip = IpUtils.getIpAddr(request);
            String client = request.getHeader("client");
            String os = request.getHeader("os");
            String uuid = request.getHeader("uuid");
            if(StringUtils.isEmpty(client)){
                // 说明是从url参数中获取
                client = request.getParameter("client");
            }
            if(StringUtils.isEmpty(os)){
                // 说明是从url参数中获取
                os = request.getParameter("os");
            }

            try {
                //*========控制台输出=========*//
                System.out.println("==============前置通知开始==============");
                System.out.println("请求方法" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName()));
                System.out.println("方法描述：" + getControllerMethodDescription(joinPoint));
                System.out.println("请求参数：" + getParams(joinPoint, request));
                System.out.println("请求ip：" + ip);

                //*========数据库日志=========*//
                SysLog sysLog = new SysLog();
                sysLog.setRequireMethod(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName());
                sysLog.setMethodDesc(getControllerMethodDescription(joinPoint));
                sysLog.setParams(getParams(joinPoint, request));
                JSONObject jsonObject = JSONUtil.parseArray(getParams(joinPoint, request)).getJSONObject(0);
                sysLog.setUserCode(jsonObject.getStr("userCode"));
                sysLog.setCompanyCode(jsonObject.getStr("companyCode"));
                sysLog.setIp(ip);
                sysLog.setClient(client);
                sysLog.setOs(os);
                sysLog.setUuid(uuid);
                //保存数据库
                sysLogService.save(sysLog);
            } catch (Exception e) {
                //记录本地异常日志
                log.error("==前置通知异常==");
                log.error("异常信息：{}", e.getMessage());
            }
        }else{
            String ip = IpUtils.getIpAddr(request);
            String client = request.getHeader("client");
            String os = request.getHeader("os");
            String uuid = request.getHeader("uuid");
            if(StringUtils.isEmpty(client)){
                // 说明是从url参数中获取
                client = request.getParameter("client");
            }
            if(StringUtils.isEmpty(os)){
                // 说明是从url参数中获取
                os = request.getParameter("os");
            }

            try {
                //*========控制台输出=========*//
                System.out.println("==============前置通知开始==============");
                System.out.println("请求方法" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName()));
                System.out.println("方法描述：" + getControllerMethodDescription(joinPoint));
                System.out.println("请求人：" + usersDto.getUserName());
                System.out.println("请求参数：" + getParams(joinPoint, request));
                System.out.println("请求ip：" + ip);

                //*========数据库日志=========*//
                SysLog sysLog = new SysLog();
                sysLog.setRequireMethod(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName());
                sysLog.setMethodDesc(getControllerMethodDescription(joinPoint));
                sysLog.setUserId(usersDto.getUserId());
                sysLog.setUserCode(usersDto.getUserCode());
                sysLog.setCompanyId(usersDto.getCompanyId());
                sysLog.setCompanyCode(usersDto.getCompanyCode());
                sysLog.setParams(getParams(joinPoint, request));
                sysLog.setIp(ip);
                sysLog.setClient(client);
                sysLog.setOs(os);
                sysLog.setUuid(uuid);
                //保存数据库
                sysLogService.save(sysLog);
            } catch (Exception e) {
                //记录本地异常日志
                log.error("==前置通知异常==");
                log.error("异常信息：{}", e.getMessage());
            }
        }
    }

    /**
     * @author changyaofang
     * @Description 获取注解中对方法的描述信息 用于Controller层注解
     * @date 2018年9月3日 上午12:01
     */
    public static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();//目标方法名
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(com.star.starboot.annotation.SysLog.class).description();
                    break;
                }
            }
        }
        return description;
    }

    /**
     * 获取请求参数 支持get,post(含与不含@RequestBody接收都可以获取)
     *
     * @param joinPoint
     * @param request
     * @return
     * @throws Exception
     */
    public String getParams(JoinPoint joinPoint, HttpServletRequest request) throws Exception {
        Gson gson = new Gson();
        String method = request.getMethod();
        String queryString = request.getQueryString();
        String params = "";
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            if ("POST".equals(method)) {
                Object object = args[0];
                //过滤掉 ServletRequest 和 ServletResponse 类型的参数
                Object paramObject = Arrays.stream(args).filter(t -> !(t instanceof ServletRequest) && !(t instanceof ServletResponse)).collect(Collectors.toList());
                params = gson.toJson(paramObject);
            } else if ("GET".equals(method)) {
                params = queryString;
            }
            params = params.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
            params = URLDecoder.decode(params, "utf-8");
        }
        return params;
    }
}
