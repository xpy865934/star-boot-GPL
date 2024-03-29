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
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 系统字典 前端控制器
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
    @ApiOperation(value = "系统字典-分页获取字典信息", notes = "系统字典-分页获取字典信息")
    @PostMapping("/queryPager")
    @RequiresPermissions("dictionary_queryPager")
    @SysLog(description = "系统字典-分页获取字典信息")
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
    @ApiOperation(value = "系统字典-保存字典信息", notes = "系统字典-保存字典信息")
    @PostMapping("/save")
    @RequiresPermissions("dictionary_save")
    @SysLog(description = "系统字典-保存字典信息", logAction = 2)
    public Result save(@RequestBody DictionaryDto dictionaryDto){
        dictionaryService.saveOrUpdate(dictionaryDto);
        return Result.success();
    }

    /**
     * 更新字典信息
     * @return
     */
    @ApiOperation(value = "系统字典-更新字典信息", notes = "系统字典-更新字典信息")
    @PostMapping("/update")
    @RequiresPermissions("dictionary_update")
    @SysLog(description = "系统字典-更新字典信息", logAction = 3)
    public Result update(@RequestBody DictionaryDto dictionaryDto){
        dictionaryService.saveOrUpdate(dictionaryDto);
        return Result.success();
    }
}

