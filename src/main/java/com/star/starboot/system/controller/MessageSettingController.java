package com.star.starboot.system.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.star.starboot.annotation.SysLog;
import com.star.starboot.common.controller.AbstractController;
import com.star.starboot.common.vo.Result;
import com.star.starboot.system.entity.MessageSetting;
import com.star.starboot.system.service.MessageSettingService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 消息配置 前端控制器
 * </p>
 *
 * @author xpy
 * @since 2020-12-03
 */
@RestController
@RequestMapping("/messageSetting")
@CrossOrigin
@Slf4j
public class MessageSettingController extends AbstractController {


    @Autowired
    private MessageSettingService messageSettingService;

    /**
     * 修改系统消息接收人配置
     *
     * @return
     */
    @ApiOperation(value = "修改系统消息接收人配置")
    @PostMapping("/update")
    @RequiresPermissions("message_setting_update")
    @SysLog(description = "修改系统消息接收人配置")
    public Result update(@RequestBody JSONObject params) {
        MessageSetting messageSetting = JSONObject.parseObject(params.toJSONString(), MessageSetting.class);
        messageSettingService.saveOrUpdate(messageSetting);
        return Result.success();
    }


    /**
     * 分页获取消息配置列表
     *
     * @return
     */
    @ApiOperation(value = "分页获取消息配置列表")
    @PostMapping("/getList")
    @RequiresPermissions("message_setting_view")
    @SysLog(description = "分页获取消息配置列表")
    public Result queryPager(@RequestBody JSONObject param) {
        Integer current = param.getInteger("current");
        Integer size = param.getInteger("size");
        MessageSetting messageSetting = param.getObject("bean", MessageSetting.class);
        IPage<MessageSetting> list = messageSettingService.queryPager(messageSetting, current, size);
        return Result.success(list);
    }
}

