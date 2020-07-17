package com.star.starboot.common.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.star.starboot.annotation.SysLog;
import com.star.starboot.common.enums.ResultCode;
import com.star.starboot.common.utils.IpUtils;
import com.star.starboot.common.vo.Result;
import com.star.starboot.config.shiro.LoginType;
import com.star.starboot.config.shiro.UserToken;
import com.star.starboot.constant.SystemConstant;
import com.star.starboot.system.dto.UsersDto;
import com.star.starboot.system.entity.Users;
import com.star.starboot.system.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version*
 * @Package com.star.starboot.common.controller
 * @Description: 通用接口层
 * @Author: xpy
 * @Date: Created in 2019年06月11日 下午8:55
 */
@RestController
@RequestMapping("/common")
@CrossOrigin
@Slf4j
public class CommonController extends  AbstractController{

    @Autowired
    private UsersService usersService;

    /**
     * 未授权跳转方法
     * @return
     */
    @RequestMapping("/unauth")
    public Result unauth(){
        SecurityUtils.getSubject().logout();
        return Result.create(ResultCode.UNAUTHO_ERROR);
    }

    /**
     * 被踢出后跳转方法
     * @return
     */
    @RequestMapping("/kickout")
    public Result kickout(){
        return Result.create(ResultCode.INVALID_TOKEN);
    }

    /**
     * 用户密码登录
     * @param param
     * @return
     */
    @PostMapping("/login")
    @SysLog(description = "用户密码登录")
    public Result login(@RequestBody JSONObject param){
        String userCode = param.getString("userCode");
        String password = param.getString("password");
        String companyCode = param.getString("companyCode");
        String clientId = param.getString("clientId");
        String appId = param.getString("appId");
        String appKey = param.getString("appKey");
        UserToken token = new UserToken(LoginType.USER_PASSWORD, userCode, password,companyCode,clientId,appId,appKey);
        return shiroLogin(token);
    }

    public Result shiroLogin(UserToken token){
        try {
            //登录不在该处处理，交由shiro处理
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);

            if (subject.isAuthenticated()) {
                JSON json = new JSONObject();
                ((JSONObject) json).put("token", subject.getSession().getId());

                // 更新用户登录ip和登录时间
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                String userId = ((Users) subject.getPrincipal()).getUserId();
                String ip = IpUtils.getIpAddr(request);
                Users users = new Users();
                users.setUserId(userId);
                users.setLastLoginDate(new Date());
                users.setLastLoginIp(ip);

                if(!StringUtils.isEmpty(token.getClientId())){
                    // 保存 clientId,appId,appKey
                    users.setClientId(token.getClientId());
                    users.setAppId(token.getAppId());
                    users.setAppKey(token.getAppkey());
                }
                usersService.updateById(users);

                // 获取用户信息，返回该用户是否注册用户  判断需要：IOS
                Users user = usersService.getById(userId);
                ((JSONObject) json).put("register", user.getRegister());

                return Result.create(ResultCode.OK, json);
            }else{
                return Result.create(ResultCode.SHIRO_ERROR);
            }
        }catch (IncorrectCredentialsException | UnknownAccountException e){
            e.printStackTrace();
            return Result.create(ResultCode.NOT_EXIST_USER_OR_ERROR_PWD);
        }catch (LockedAccountException e){
            return Result.create(ResultCode.USER_FROZEN);
        }catch (DisabledAccountException e){
            return Result.create(ResultCode.USER_DISABLED);
        }catch (Exception e){
            log.error(e.getMessage());
            return Result.create(ResultCode.SYSTEM_ERR);
        }
    }

    /**
     * 退出登录
     * @return
     */
    @RequestMapping("/logout")
    @SysLog(description = "退出登录")
    public Result logout(){
        SecurityUtils.getSubject().logout();
        return Result.create(ResultCode.OK);
    }

    /**
     * 注册用户  判断需要：IOS
     * @return
     */
    @PostMapping("/register")
    @SysLog(description = "注册用户")
    public Result register(@RequestBody UsersDto usersDto){
        usersDto.setRegister(SystemConstant.REGISTER);
        usersService.register(usersDto);
        return Result.success("注册成功");
    }
}
