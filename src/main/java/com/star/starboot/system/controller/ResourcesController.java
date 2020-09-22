package com.star.starboot.system.controller;


import com.alibaba.fastjson.JSONObject;
import com.star.starboot.annotation.SysLog;
import com.star.starboot.common.controller.AbstractController;
import com.star.starboot.common.vo.Result;
import com.star.starboot.system.dto.ResourcesDto;
import com.star.starboot.system.entity.Resources;
import com.star.starboot.system.service.ResourcesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户菜单功能资源表 前端控制器
 * </p>
 *
 * @author xpy
 * @since 2020-07-02
 */
@RestController
@RequestMapping("/resources")
@CrossOrigin
@Slf4j
public class ResourcesController extends AbstractController {

    @Autowired
    private ResourcesService resourcesService;

    /**
     * 获取资源列表
     * @return
     */
    @PostMapping("/queryList")
//    @RequiresPermissions("resourcesQueryList")
    @SysLog(description = "获取资源列表")
    public Result queryList(@RequestBody JSONObject param){
        ResourcesDto resourcesDto = param.getObject("bean", ResourcesDto.class);
        List<ResourcesDto> list = resourcesService.queryList(resourcesDto);
        return Result.success(list);
    }

    /**
     * 保存菜单资源信息
     * @return
     */
    @PostMapping("/save")
//    @RequiresPermissions("resourcesQueryList")
    @SysLog(description = "保存菜单资源信息")
    public Result save(@RequestBody ResourcesDto resourcesDto){
        resourcesService.saveOrUpdate(resourcesDto);
        return Result.success();
    }

    /**
     * 更新菜单资源信息
     * @return
     */
    @PostMapping("/update")
//    @RequiresPermissions("resourcesQueryList")
    @SysLog(description = "更新菜单资源信息")
    public Result update(@RequestBody ResourcesDto resourcesDto){
        resourcesService.saveOrUpdate(resourcesDto);
        return Result.success();
    }

    /**
     * 更新资源是否启用
     * @return
     */
    @PostMapping("/updateUsed")
//    @RequiresPermissions("resourcesQueryList")
    @SysLog(description = "更新资源是否启用")
    public Result updateUsed(@RequestBody ResourcesDto resourcesDto){
        Resources resources = new Resources();
        resources.setResourcesId(resourcesDto.getResourcesId());
        resources.setUsed(resourcesDto.getUsed());
        resourcesService.updateById(resources);
        return Result.success();
    }


    /**
     * 删除资源
     * @return
     */
    @GetMapping("/deleteById/{resourceId}")
//    @RequiresPermissions("usersDeleted")
    @SysLog(description = "删除资源")
    public Result deleteById(@PathVariable String resourceId){
        resourcesService.deleteById(resourceId);
        return Result.success();
    }

}

