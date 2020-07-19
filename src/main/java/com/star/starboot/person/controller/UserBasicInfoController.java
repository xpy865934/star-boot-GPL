package com.star.starboot.person.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.starboot.annotation.SysLog;
import com.star.starboot.common.controller.AbstractController;
import com.star.starboot.common.enums.ResultCode;
import com.star.starboot.common.utils.ShiroUtils;
import com.star.starboot.common.vo.Result;
import com.star.starboot.person.dto.UserBasicInfoDto;
import com.star.starboot.person.entity.UserBasicInfo;
import com.star.starboot.person.service.UserBasicInfoService;
import com.star.starboot.system.dto.UsersDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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
     * 新增个人信息
     * @return
     */
    @PostMapping("/save")
    @RequiresPermissions("userBasicInfoSave")
    @SysLog(description = "保存个人信息")
    public Result save(@RequestBody UserBasicInfo userBasicInfoDto){
        UsersDto userInfo = ShiroUtils.build().getUserInfo();
        userBasicInfoDto.setUserId(userInfo.getUserId());
        userBasicInfoDto.setCreateBy(userInfo.getUserId());
        userBasicInfoService.saveOrUpdate(userBasicInfoDto);
        return Result.create(ResultCode.SUCCESS_SAVE);
    }

    /**
     * 修改个人信息
     * @return
     */
    @PostMapping("/update")
    @RequiresPermissions("userBasicInfoUpdate")
    @SysLog(description = "修改个人信息")
    public Result update(@RequestBody UserBasicInfoDto userBasicInfoDto){
        UsersDto userInfo = ShiroUtils.build().getUserInfo();
        userBasicInfoDto.setUpdateAt(new Date());
        userBasicInfoDto.setUpdateBy(userInfo.getUserId());
        userBasicInfoService.saveOrUpdate(userBasicInfoDto);
        return Result.create(ResultCode.SUCCESS_SAVE);
    }

    /**
     * 查询个人信息
     * @return
     */
    @PostMapping("/query")
    @RequiresPermissions("userBasicInfoQuery")
    @SysLog(description = "查询个人信息")
    public Result query(){
        UsersDto userInfo = ShiroUtils.build().getUserInfo();
        UserBasicInfoDto userBasicInfoDto = new UserBasicInfoDto();
        LambdaQueryWrapper<UserBasicInfo> wrapper = new LambdaQueryWrapper();
        wrapper.eq(UserBasicInfo::getUserId,userInfo.getUserId());
        UserBasicInfo userBasicInfo = userBasicInfoService.getOne(wrapper);
        if(!StringUtils.isEmpty(userBasicInfo)){
            BeanUtils.copyProperties(userBasicInfo,userBasicInfoDto);
        }
        userBasicInfoDto.setUserName(userInfo.getUserName());
        userBasicInfoDto.setUserCode(userInfo.getUserCode());
        return Result.create(ResultCode.OK,userBasicInfoDto);
    }
}

