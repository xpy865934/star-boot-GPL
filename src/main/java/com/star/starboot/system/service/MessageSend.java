package com.star.starboot.system.service;

import com.star.starboot.system.entity.Message;

import java.util.List;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.system.service
 * @Description: 消息发送接口
 * @Author: xpy
 * @Date: Created in 2020年12月04日 9:17 上午
 */
public interface MessageSend {

    /**
     * 发送消息
     * @param tos
     * @param msg
     * @param from
     */
    void sendMsg(List<String> tos, Message msg, String from);
}
