package com.star.starboot.system.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.star.starboot.annotation.SysLog;
import com.star.starboot.common.controller.AbstractController;
import com.star.starboot.common.vo.Result;
import com.star.starboot.system.dto.FirstDictDto;
import com.star.starboot.system.service.FirstDictService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 一级字典 前端控制器
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
@RestController
@RequestMapping("/firstDict")
@CrossOrigin
@Slf4j
public class FirstDictController extends AbstractController {

    @Autowired
    private FirstDictService firstDictService;

    /**
     * 分页获取一级字典信息
     * @return
     */
    @ApiOperation("分页获取一级字典信息")
    @PostMapping("/queryPager")
    @RequiresPermissions("firstDict:queryPager")
    @SysLog(description = "分页获取一级字典信息")
    public Result queryPager(@RequestBody JSONObject param){
        Integer current = param.getInteger("current");
        Integer size = param.getInteger("size");
        FirstDictDto firstDictDto = param.getObject("bean", FirstDictDto.class);
        IPage<FirstDictDto> list = firstDictService.queryPager(firstDictDto,current,size);
        return Result.success(list);
    }

    /**
     * 保存一级字典信息
     * @return
     */
    @ApiOperation("保存一级字典信息")
    @PostMapping("/save")
    @RequiresPermissions("firstDict:save")
    @SysLog(description = "保存一级字典信息")
    public Result save(@RequestBody FirstDictDto firstDictDto){
        firstDictService.saveOrUpdate(firstDictDto);
        return Result.success();
    }

    /**
     * 更新一级字典信息
     * @return
     */
    @ApiOperation("更新一级字典信息")
    @PostMapping("/update")
    @RequiresPermissions("firstDict:update")
    @SysLog(description = "更新一级字典信息")
    public Result update(@RequestBody FirstDictDto firstDictDto){
        firstDictService.saveOrUpdate(firstDictDto);
        return Result.success();
    }
}

