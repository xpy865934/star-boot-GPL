package com.star.starboot.person.controller;


import com.star.starboot.annotation.SysLog;
import com.star.starboot.common.controller.AbstractController;
import com.star.starboot.common.enums.ResultCode;
import com.star.starboot.common.vo.Result;
import com.star.starboot.person.dto.UserBasicInfoDto;
import com.star.starboot.person.entity.UserBasicInfo;
import com.star.starboot.person.service.UserBasicInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户基本信息 前端控制器
 * </p>
 *
 * @author xpy
 * @since 2020-07-19
 */
@RestController
@RequestMapping("/userBasicInfo")
@CrossOrigin
@Slf4j
public class UserBasicInfoController extends AbstractController {
    @Autowired
    private UserBasicInfoService userBasicInfoService;

    /**
     * 新增客户信息
     * @return
     */
    @PostMapping("/save")
    @RequiresPermissions("userBasicInfoSave")
    @SysLog(description = "保存个人信息")
    public Result save(@RequestBody UserBasicInfo userBasicInfoDto){
        userBasicInfoService.save(userBasicInfoDto);
        return Result.create(ResultCode.SUCCESS_SAVE);
    }

    /**
     * 新增客户信息
     * @return
     */
    @PostMapping("/update")
    @RequiresPermissions("userBasicInfoUpdate")
    @SysLog(description = "修改个人信息")
    public Result update(@RequestBody UserBasicInfoDto userBasicInfoDto){
        userBasicInfoService.saveOrUpdate(userBasicInfoDto);
        return Result.create(ResultCode.OK);
    }
}

