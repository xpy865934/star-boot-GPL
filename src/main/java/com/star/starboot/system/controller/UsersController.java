package com.star.starboot.system.controller;


import com.star.starboot.annotation.SysLog;
import com.star.starboot.common.enums.ResultCode;
import com.star.starboot.common.utils.ShiroUtils;
import com.star.starboot.common.vo.Result;
import com.star.starboot.config.shiro.UserPasswordRealm;
import com.star.starboot.system.dto.UsersDto;
import com.star.starboot.system.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.star.starboot.common.controller.AbstractController;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
@RestController
@RequestMapping("/users")
@CrossOrigin
@Slf4j
public class UsersController extends AbstractController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private UserPasswordRealm userPasswordRealm;
    /**
     * 获取登录用户信息
     * @return
     */
    @PostMapping("/getUserInfo")
    @SysLog(description = "获取登录用户信息")
    public Result getUserInfo(){
        UsersDto userInfo = ShiroUtils.build().getUserInfo();
        if(!StringUtils.isEmpty(userInfo)){
            AuthorizationInfo authorizationInfo = userPasswordRealm.getUserAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
            if(!StringUtils.isEmpty(authorizationInfo.getRoles())){
                userInfo.setRoles(authorizationInfo.getRoles().toArray());
            }
            if(!StringUtils.isEmpty(authorizationInfo.getStringPermissions())){
                userInfo.setPermissions(authorizationInfo.getStringPermissions().toArray());
            }
            return Result.success(userInfo);
        }else{
            return Result.create(ResultCode.USER_INFO_LOSE_ERROR);
        }
    }
}

