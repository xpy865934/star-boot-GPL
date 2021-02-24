package com.star.starboot.system.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.star.starboot.annotation.SysLog;
import com.star.starboot.common.controller.AbstractController;
import com.star.starboot.common.vo.Result;
import com.star.starboot.system.service.SysLogService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 系统日志 前端控制器
 * </p>
 *
 * @author xpy
 * @since 2020-07-01
 */
@RestController
@RequestMapping("/sysLog")
@CrossOrigin
@Slf4j
public class SysLogController extends AbstractController {

    @Autowired
    private SysLogService sysLogService;


    /**
     * 分页获取系统日志信息
     * @return
     */
    @ApiOperation(value = "系统日志-分页获取系统日志信息", notes = "系统日志-分页获取系统日志信息")
    @PostMapping("/queryPager")
    @RequiresPermissions("sysLog_queryPager")
    @SysLog(description = "系统日志-分页获取系统日志信息")
    public Result queryPager(@RequestBody JSONObject param){
        Integer current = param.getInteger("current");
        Integer size = param.getInteger("size");
        com.star.starboot.system.entity.SysLog sysLog = param.getObject("bean", com.star.starboot.system.entity.SysLog.class);
        IPage<com.star.starboot.system.entity.SysLog> list = sysLogService.queryPager(sysLog,current,size);
        return Result.success(list);
    }
}

