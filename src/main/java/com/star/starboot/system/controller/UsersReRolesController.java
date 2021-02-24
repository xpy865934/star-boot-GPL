package com.star.starboot.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.starboot.annotation.SysLog;
import com.star.starboot.common.controller.AbstractController;
import com.star.starboot.common.vo.Result;
import com.star.starboot.system.dto.UsersReRolesDto;
import com.star.starboot.system.entity.UsersReRoles;
import com.star.starboot.system.service.UsersReRolesService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户和角色关联关系 前端控制器
 * </p>
 *
 * @author xpy
 * @since 2020-07-05
 */
@RestController
@RequestMapping("/usersReRoles")
@CrossOrigin
@Slf4j
public class UsersReRolesController extends AbstractController {

    @Autowired
    private UsersReRolesService usersReRolesService;


    /**
     * 获取用户所有的角色信息
     * @return
     */
    @ApiOperation(value = "用户和角色关联关系-获取用户所有的角色信息", notes = "用户和角色关联关系-获取用户所有的角色信息")
    @PostMapping("/getRolesByUserId")
    @SysLog(description = "用户和角色关联关系-获取用户所有的角色信息")
    public Result getRolesByUserId(@RequestBody UsersReRolesDto usersReRolesDto){
        LambdaQueryWrapper<UsersReRoles> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UsersReRoles::getUserId, usersReRolesDto.getUserId());
        List<UsersReRoles> list = usersReRolesService.list(wrapper);
        return Result.success(list);
    }

    /**
     * 保存用户和角色关系
     * @return
     */
    @ApiOperation(value = "用户和角色关联关系-保存用户和角色关系", notes = "用户和角色关联关系-保存用户和角色关系")
    @PostMapping("/save")
    @RequiresPermissions("usersReRoles_save")
    @SysLog(description = "用户和角色关联关系-保存用户和角色关系")
    public Result save(@RequestBody UsersReRolesDto usersReRolesDto){
        usersReRolesService.inserOrUpdate(usersReRolesDto);
        return Result.success();
    }
}

