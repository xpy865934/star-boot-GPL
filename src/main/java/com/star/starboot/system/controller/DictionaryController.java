package com.star.starboot.system.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.star.starboot.annotation.SysLog;
import com.star.starboot.common.controller.AbstractController;
import com.star.starboot.common.vo.Result;
import com.star.starboot.system.dto.DictionaryDto;
import com.star.starboot.system.service.DictionaryService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 系统字典表 前端控制器
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
@RestController
@RequestMapping("/dictionary")
@CrossOrigin
@Slf4j
public class DictionaryController extends AbstractController {

    @Autowired
    private DictionaryService dictionaryService;

    /**
     * 分页获取字典信息
     * @return
     */
    @ApiOperation("分页获取字典信息")
    @PostMapping("/queryPager")
//    @RequiresPermissions("rolesQueryPager")
    @SysLog(description = "分页获取字典信息")
    public Result queryPager(@RequestBody JSONObject param){
        Integer current = param.getInteger("current");
        Integer size = param.getInteger("size");
        DictionaryDto dictionaryDto = param.getObject("bean", DictionaryDto.class);
        IPage<DictionaryDto> list = dictionaryService.queryPager(dictionaryDto,current,size);
        return Result.success(list);
    }

    /**
     * 保存字典信息
     * @return
     */
    @ApiOperation("保存字典信息")
    @PostMapping("/save")
//    @RequiresPermissions("resourcesQueryList")
    @SysLog(description = "保存字典信息")
    public Result save(@RequestBody DictionaryDto dictionaryDto){
        dictionaryService.saveOrUpdate(dictionaryDto);
        return Result.success();
    }

    /**
     * 更新字典信息
     * @return
     */
    @ApiOperation("更新字典信息")
    @PostMapping("/update")
//    @RequiresPermissions("resourcesQueryList")
    @SysLog(description = "更新字典信息")
    public Result update(@RequestBody DictionaryDto dictionaryDto){
        dictionaryService.saveOrUpdate(dictionaryDto);
        return Result.success();
    }
}

