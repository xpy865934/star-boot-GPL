package com.star.starboot.system.controller;


import com.alibaba.fastjson.JSONObject;
import com.star.starboot.annotation.SysLog;
import com.star.starboot.common.controller.AbstractController;
import com.star.starboot.common.vo.Result;
import com.star.starboot.system.entity.Company;
import com.star.starboot.system.service.CompanyService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 公司信息 前端控制器
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
@RestController
@RequestMapping("/company")
@CrossOrigin
@Slf4j
public class CompanyController extends AbstractController {

    @Autowired
    private CompanyService companyService;

    /**
     * 获取所有的公司信息
     * @return
     */
    @ApiOperation("获取所有的公司列表，不分页")
    @PostMapping("/getList")
    @SysLog(description = "获取所有的公司列表，不分页")
    public Result getList(@RequestBody JSONObject param){
        List<Company> list = companyService.list();
        return Result.success(list);
    }
}

