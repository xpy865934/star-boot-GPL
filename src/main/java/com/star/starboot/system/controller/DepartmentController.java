package com.star.starboot.system.controller;


import com.star.starboot.annotation.SysLog;
import com.star.starboot.common.enums.ResultCode;
import com.star.starboot.common.utils.ShiroUtils;
import com.star.starboot.common.vo.Result;
import com.star.starboot.system.dto.CompanyDto;
import com.star.starboot.system.dto.UsersDto;
import com.star.starboot.system.entity.Company;
import com.star.starboot.system.service.DepartmentService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.star.starboot.common.controller.AbstractController;

import java.util.List;

/**
 * <p>
 * 组织架构 前端控制器
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
@RestController
@RequestMapping("/department")
@CrossOrigin
@Slf4j
public class DepartmentController extends AbstractController {

    @Autowired
    private DepartmentService departmentService;


    /**
     * 获取所有的组织架构信息树形菜单
     *
     * @return
     */
    @ApiOperation(value = "获取所有的组织架构信息树形菜单")
    @GetMapping("/getDepartmentTree")
    @RequiresPermissions("department_getDepartmentsTree")
    @SysLog(description = "获取所有的组织架构信息树形菜单")
    public Result getDepartmentTree() {
        UsersDto userInfo = ShiroUtils.build().getUserInfo();
        List<CompanyDto> result = departmentService.getDepartmentTree(userInfo.getCompanyId());
        return Result.success(result);
    }
}

