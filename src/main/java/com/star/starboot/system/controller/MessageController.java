package com.star.starboot.system.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.star.starboot.annotation.SysLog;
import com.star.starboot.common.controller.AbstractController;
import com.star.starboot.common.vo.Result;
import com.star.starboot.system.entity.Message;
import com.star.starboot.system.service.MessageService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 消息管理 前端控制器
 * </p>
 *
 * @author xpy
 * @since 2020-12-03
 */
@RestController
@RequestMapping("/message")
@CrossOrigin
@Slf4j
public class MessageController extends AbstractController {

    @Autowired
    private MessageService messageService;

    /**
     * 分页获取该用户的个人消息
     *
     * @return
     */
    @ApiOperation(value = "消息管理-分页获取该用户的个人消息", notes = "消息管理-分页获取该用户的个人消息")
    @PostMapping("/getUserMessagePager")
    @SysLog(description = "消息管理-分页获取该用户的个人消息")
    public Result getUserMessagePager(@RequestBody JSONObject param) {
        Integer current = param.getInteger("current");
        Integer size = param.getInteger("size");
        Message message = param.getObject("bean", Message.class);
        IPage<Message> list = messageService.getUserMessagePager(message, current, size);
        return Result.success(list);
    }

    /**
     * 不分页获取该用户的个人消息
     *
     * @return
     */
    @ApiOperation(value = "消息管理-不分页获取该用户的个人消息", notes = "消息管理-不分页获取该用户的个人消息")
    @PostMapping("/getUserMsgList")
    @SysLog(description = "不分页获取该用户的个人消息")
    public Result getUserMsgList(@RequestBody JSONObject param) {
        Message message = param.getObject("bean", Message.class);
        List<Message> list = messageService.getUserMsgList(message);
        return Result.success(list);
    }

    /**
     * 更新消息为已读
     *
     * @return
     */
    @ApiOperation(value = "消息管理-更新消息为已读", notes = "消息管理-更新消息为已读")
    @PostMapping("/upadteUserMessageRead")
    @SysLog(description = "消息管理-更新消息为已读")
    public Result upadteUserMessageRead(@RequestBody JSONObject params) {
        Message message = JSONObject.parseObject(params.toJSONString(), Message.class);
        messageService.upadteUserMessageRead(message);
        return Result.success();
    }

    /**
     * 更新所有消息为已读
     *
     * @return
     */
    @ApiOperation(value = "消息管理-更新所有消息为已读", notes = "消息管理-更新所有消息为已读")
    @PostMapping("/updateAllRead")
    @SysLog(description = "消息管理-更新所有消息为已读")
    public Result updateAllRead(@RequestBody JSONObject params) {
        Message message = JSONObject.parseObject(params.toJSONString(), Message.class);
        messageService.updateAllRead(message);
        return Result.success();
    }

    /**
     * 获取消息数量
     *
     * @return
     */
    @ApiOperation(value = "消息管理-获取消息数量", notes = "消息管理-获取消息数量")
    @PostMapping("/getMsgCount")
    @SysLog(description = "消息管理-获取消息数量")
    public Result getMsgCount(@RequestBody JSONObject params) {
        List<Map<String, Integer>> list = messageService.getMsgCount();
        return Result.success(list);
    }
}

