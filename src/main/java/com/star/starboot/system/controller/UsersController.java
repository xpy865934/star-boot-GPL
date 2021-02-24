package com.star.starboot.system.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.star.starboot.annotation.SysLog;
import com.star.starboot.common.controller.AbstractController;
import com.star.starboot.common.enums.ResultCode;
import com.star.starboot.common.utils.ShiroUtils;
import com.star.starboot.common.vo.Result;
import com.star.starboot.config.shiro.AuthorizationRealm;
import com.star.starboot.system.dto.UsersDto;
import com.star.starboot.system.service.UsersService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户信息 前端控制器
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
    private AuthorizationRealm authorizationRealm;

    /**
     * 分页获取用户信息
     * @return
     */
    @ApiOperation(value = "用户信息-分页获取用户信息", notes = "用户信息-分页获取用户信息")
    @PostMapping("/queryPager")
    @RequiresPermissions("users_queryPager")
    @SysLog(description = "用户信息-分页获取用户信息")
    public Result queryPager(@RequestBody JSONObject param){
        Integer current = param.getInteger("current");
        Integer size = param.getInteger("size");
        UsersDto usersDto = param.getObject("bean", UsersDto.class);
        IPage<UsersDto> list = usersService.queryPager(usersDto,current,size);
        return Result.success(list);
    }

    /**
     * 获取登录用户信息
     * @return
     */
    @ApiOperation(value = "用户信息-获取登录用户信息", notes = "用户信息-获取登录用户信息")
    @PostMapping("/getUserInfo")
    @SysLog(description = "用户信息-获取登录用户信息")
    public Result getUserInfo(){
        UsersDto userInfo = ShiroUtils.build().getUserInfo();
        if(!StringUtils.isEmpty(userInfo)){
            // 该处不可删除，在ShiroUtils.build().getUserInfo();中虽然把角色和权限信息加入进去，但是部分时候获取的为空，所以此处不可以删除
            SimpleAuthorizationInfo authorizationInfo = authorizationRealm.getUserAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
            if(!StringUtils.isEmpty(authorizationInfo.getRoles())){
                userInfo.setRoles(authorizationInfo.getRoles());
            }
            if(!StringUtils.isEmpty(authorizationInfo.getStringPermissions())){
                userInfo.setPermissions(authorizationInfo.getStringPermissions());
            }
            return Result.success(userInfo);
        }else{
            return Result.create(ResultCode.USER_INFO_LOSE_ERROR);
        }
    }

    /**
     * 删除用户
     * @return
     */
    @ApiOperation(value = "用户信息-删除用户", notes = "用户信息-删除用户")
    @GetMapping("/deleteById/{userId}")
    @RequiresPermissions("users_deleteById")
    @SysLog(description = "用户信息-删除用户")
    public Result deleteById(@PathVariable String userId){
        usersService.deleteById(userId);
        return Result.success();
    }


    /**
     * 查询所有用户
     * @return
     */
    @ApiOperation(value = "用户信息-查询所有用户", notes = "用户信息-查询所有用户")
    @PostMapping("/queryList")
    @SysLog(description = "用户信息-查询所有用户")
    public Result queryList(@RequestBody JSONObject param){
        List<UsersDto> list = usersService.queryList();
        return Result.success(list);
    }


    /**
     * 修改密码
     * @return
     */
    @ApiOperation(value = "用户信息-修改密码", notes = "用户信息-修改密码")
    @PostMapping("/changePassword")
    @SysLog(description = "用户信息-修改密码")
    public Result changePassword(@RequestBody UsersDto usersDto){
        usersService.changePassword(usersDto);
        return Result.success();
    }


    /**
     * 保存用户信息
     * @return
     */
    @ApiOperation(value = "用户信息-保存用户信息", notes = "用户信息-保存用户信息")
    @PostMapping("/save")
    @RequiresPermissions("users_save")
    @SysLog(description = "用户信息-保存用户信息")
    public Result save(@RequestBody UsersDto usersDto){
        usersService.insertOrUpdate(usersDto);
        return Result.success();
    }

    /**
     * 更新用户信息
     * @return
     */
    @ApiOperation(value = "用户信息-更新用户信息", notes = "用户信息-更新用户信息")
    @PostMapping("/update")
    @RequiresPermissions("users_update")
    @SysLog(description = "用户信息-更新用户信息")
    public Result update(@RequestBody UsersDto usersDto){
        usersService.insertOrUpdate(usersDto);
        return Result.success();
    }

    /**
     * 根据id获取信息
     * @return
     */
    @ApiOperation(value = "用户信息-根据id获取信息", notes = "用户信息-根据id获取信息")
    @GetMapping("/queryById/{userId}")
    @SysLog(description = "用户信息-根据id获取信息")
    public Result queryById(@PathVariable String userId){
        UsersDto usersDto =  usersService.queryById(userId);
        return Result.success(usersDto);
    }
}

