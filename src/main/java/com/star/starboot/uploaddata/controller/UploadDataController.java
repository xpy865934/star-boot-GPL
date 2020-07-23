package com.star.starboot.uploaddata.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.star.starboot.annotation.SysLog;
import com.star.starboot.common.controller.AbstractController;
import com.star.starboot.common.enums.ResultCode;
import com.star.starboot.common.utils.ShiroUtils;
import com.star.starboot.common.vo.Result;
import com.star.starboot.person.entity.UserBasicInfo;
import com.star.starboot.person.service.UserBasicInfoService;
import com.star.starboot.system.dto.UsersDto;
import com.star.starboot.uploaddata.dto.UploadDataDto;
import com.star.starboot.uploaddata.entity.UploadData;
import com.star.starboot.uploaddata.service.UploadDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 上报的数据 前端控制器
 * </p>
 *
 * @author xpy
 * @since 2020-07-19
 */
@RestController
@RequestMapping("/uploadData")
@CrossOrigin
@Slf4j
public class UploadDataController extends AbstractController {

    @Autowired
    private UploadDataService uploadDataService;

    @Autowired
    private UserBasicInfoService userBasicInfoService;

    /**
     * 新增上传ICU信息
     * @return
     */
    @PostMapping("/save")
    @RequiresPermissions("uploadDataSave")
    @SysLog(description = "新增上传ICU信息")
    public Result save(@RequestBody UploadDataDto uploadDataDto){
        UsersDto userInfo = ShiroUtils.build().getUserInfo();

        if(StringUtils.isEmpty(uploadDataDto.getSbsj())){
            return Result.create(ResultCode.ERROR_PARAMS);
        }
        if(new Date().before(uploadDataDto.getSbsj())){
            // 当前时间在所选时间之前，则不能填报
            return  Result.create(ResultCode.SYSTEM_ERR.getCode(),"月份异常，不可以提前申报");
        }

        // 判断是否已经存在上传的ICU信息
        UploadData uploadData = uploadDataService.queryByDate(uploadDataDto, userInfo);
        if(!StringUtils.isEmpty(uploadData)){
            return Result.create(ResultCode.SYSTEM_ERR.getCode(),"当月已经存在数据，不可以重复新增");
        }
        uploadDataDto.setUserId(userInfo.getUserId());
        uploadDataDto.setCreateBy(userInfo.getUserId());
        uploadDataService.saveOrUpdate(uploadDataDto);
        return Result.create(ResultCode.SUCCESS_SAVE);
    }

    /**
     * 修改ICU信息
     * @return
     */
    @PostMapping("/update")
    @RequiresPermissions("uploadDataUpdate")
    @SysLog(description = "修改ICU信息")
    public Result update(@RequestBody UploadDataDto uploadDataDto){
        UsersDto userInfo = ShiroUtils.build().getUserInfo();
        uploadDataDto.setUpdateAt(new Date());
        uploadDataDto.setUpdateBy(userInfo.getUserId());
        uploadDataService.saveOrUpdate(uploadDataDto);
        return Result.create(ResultCode.SUCCESS_SAVE);
    }

    /**
     * 分页获取上报信息
     * @return
     */
    @PostMapping("/queryPager")
    @RequiresPermissions("uploadDataQueryPager")
    @SysLog(description = "分页获取上报信息")
    public Result queryPager(@RequestBody JSONObject param){
            UsersDto userInfo = ShiroUtils.build().getUserInfo();
            Integer current = param.getInteger("current");
            Integer size = param.getInteger("size");
            UploadDataDto uploadDataDto = param.getObject("bean", UploadDataDto.class);
            uploadDataDto.setUserId(userInfo.getUserId());
            IPage<UploadDataDto> list = uploadDataService.queryPager(uploadDataDto,current,size);
            return Result.success(list);
    }

    /**
     * 上报ICU信息
     * @return
     */
    @PostMapping("/upload")
    @RequiresPermissions("uploadDataUpload")
    @SysLog(description = "上报ICU信息")
    public Result upload(@RequestBody UploadDataDto uploadDataDto){
        UsersDto userInfo = ShiroUtils.build().getUserInfo();
        uploadDataDto.setUpdateAt(new Date());
        uploadDataDto.setUpdateBy(userInfo.getUserId());
        uploadDataDto.setUserId(userInfo.getUserId());
        uploadDataService.saveOrUpdate(uploadDataDto);
        return Result.create(ResultCode.SUCCESS_UPLOAD);
    }
    /**
     * 日期查询ICU信息
     * @return
     */
    @PostMapping("/queryByDate")
    @RequiresPermissions("uploadDataQueryByDate")
    @SysLog(description = "日期查询ICU信息")
    public Result queryByDate(@RequestBody UploadDataDto data){
        UsersDto userInfo = ShiroUtils.build().getUserInfo();
        UploadDataDto uploadDataDto = new UploadDataDto();

        UploadData uploadData = null;
        if(!StringUtils.isEmpty(data.getSbsj())){
            uploadData = uploadDataService.queryByDate(data, userInfo);
        } else {
            return Result.create(ResultCode.ERROR_PARAMS);
        }

        if(!StringUtils.isEmpty(uploadData)){
            BeanUtils.copyProperties(uploadData,uploadDataDto);
        } else {
            // 第一次填写，设置医师、护士等人数
            LambdaQueryWrapper<UserBasicInfo> userWrapper = new LambdaQueryWrapper();
            userWrapper.eq(UserBasicInfo::getUserId,userInfo.getUserId());
            UserBasicInfo userBasicInfo = userBasicInfoService.getOne(userWrapper);
            if(!StringUtils.isEmpty(userBasicInfo)){
                if(!StringUtils.isEmpty(userBasicInfo.getBedTotal())){
                    uploadDataDto.setYyzcws(userBasicInfo.getBedTotal());
                }
                if(!StringUtils.isEmpty(userBasicInfo.getIcuBedTotal())){
                    uploadDataDto.setIcuCws(userBasicInfo.getIcuBedTotal());
                }
                if(!StringUtils.isEmpty(userBasicInfo.getYsrs())){
                    uploadDataDto.setIcuZkys(userBasicInfo.getYsrs());
                }
                if(!StringUtils.isEmpty(userBasicInfo.getHsrs())){
                    uploadDataDto.setIcuHszs(userBasicInfo.getHsrs());
                }
            }
        }
        return Result.success(uploadDataDto);
    }
}

