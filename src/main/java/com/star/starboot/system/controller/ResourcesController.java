package com.star.starboot.system.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.star.starboot.annotation.SysLog;
import com.star.starboot.common.vo.Result;
import com.star.starboot.system.dto.ResourcesDto;
import com.star.starboot.system.dto.UsersDto;
import com.star.starboot.system.service.ResourcesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.star.starboot.common.controller.AbstractController;

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
}

