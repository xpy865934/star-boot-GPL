package com.star.starboot.system.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.star.starboot.annotation.SysLog;
import com.star.starboot.common.vo.Result;
import com.star.starboot.system.dto.ResourcesDto;
import com.star.starboot.system.dto.RolesDto;
import com.star.starboot.system.dto.UsersDto;
import com.star.starboot.system.entity.Resources;
import com.star.starboot.system.service.RolesReResourcesService;
import com.star.starboot.system.service.RolesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.star.starboot.common.controller.AbstractController;

import java.util.List;

/**
 * <p>
 * 角色信息 前端控制器
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
@RestController
@RequestMapping("/roles")
@CrossOrigin
@Slf4j
public class RolesController extends AbstractController {

    @Autowired
    private RolesService rolesService;

    @Autowired
    private RolesReResourcesService rolesReResourcesService;


    /**
     * 分页获取角色信息
     * @return
     */
    @PostMapping("/queryPager")
//    @RequiresPermissions("rolesQueryPager")
    @SysLog(description = "分页获取角色信息")
    public Result queryPager(@RequestBody JSONObject param){
        Integer current = param.getInteger("current");
        Integer size = param.getInteger("size");
        RolesDto rolesDto = param.getObject("bean", RolesDto.class);
        IPage<RolesDto> list = rolesService.queryPager(rolesDto,current,size);
        return Result.success(list);
    }

    /**
     * 保存角色信息
     * @return
     */
    @PostMapping("/save")
//    @RequiresPermissions("resourcesQueryList")
    @SysLog(description = "保存角色信息")
    public Result save(@RequestBody RolesDto rolesDto){
        rolesService.saveOrUpdate(rolesDto);
        return Result.success();
    }

    /**
     * 更新角色信息
     * @return
     */
    @PostMapping("/update")
//    @RequiresPermissions("resourcesQueryList")
    @SysLog(description = "更新角色信息")
    public Result update(@RequestBody RolesDto rolesDto){
        rolesService.saveOrUpdate(rolesDto);
        return Result.success();
    }

    /**
     * 根据角色id获取角色资源信息
     * @return
     */
    @PostMapping("/getResourcesByRoleTid")
//    @RequiresPermissions("resourcesQueryList")
    @SysLog(description = "根据角色id获取角色资源信息")
    public Result getResourcesByRoleTid(@RequestBody RolesDto rolesDto){
        List<Resources> resources = rolesReResourcesService.getResourcesByRoleTid(rolesDto.getRoleId());
        return Result.success(resources);
    }

    /**
     * 更新角色资源信息
     * @return
     */
    @PostMapping("/updateRoleResources")
//    @RequiresPermissions("resourcesQueryList")
    @SysLog(description = "更新角色资源信息")
    public Result updateRoleResources(@RequestBody JSONObject param){
        String roleId = param.getString("roleId");
        List<String> checked = param.getJSONArray("checked").toJavaList(String.class);
        rolesReResourcesService.save(roleId,checked);
        return Result.success();
    }
}

