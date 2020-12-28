package com.star.starboot.system.service.impl;

import cn.hutool.json.JSONUtil;
import com.star.starboot.config.WebSocketServer;
import com.star.starboot.constant.SystemConstant;
import com.star.starboot.system.entity.Message;
import com.star.starboot.system.service.MessageSend;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.system.service.impl
 * @Description: app消息发送
 * @Author: xpy
 * @Date: Created in 2020年12月04日 10:17 上午
 */
@Service(SystemConstant.MSG_SYSTEM)
public class SystemMessageSendImpl implements MessageSend {

    @Override
    public void sendMsg(List<String> tos, Message msg, String from) {
        WebSocketServer.sendMsg(tos, JSONUtil.toJsonStr(msg));
    }
}
