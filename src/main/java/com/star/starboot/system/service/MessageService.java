package com.star.starboot.system.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.star.starboot.system.entity.Message;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 消息 服务类
 * </p>
 *
 * @author xpy
 * @since 2020-12-03
 */
public interface MessageService extends IService<Message> {

    /**
     * 分页获取该用户的个人消息
     * @param message
     * @param current
     * @param size
     * @return
     */
    IPage<Message> getUserMessagePager(Message message, Integer current, Integer size);

    /**
     * 不分页获取该用户个人信息
     * @param message
     * @return
     */
    List<Message> getUserMsgList(Message message);

    /**
     * 更新消息为已读
     * @param message
     */
    void upadteUserMessageRead(Message message);

    /**
     * 更新所有消息为已读
     * @param message
     */
    void updateAllRead(Message message);

    /**
     * 获取未读消息数量
     * @return
     */
    List<Map<String, Integer>> getMsgCount();

    /**
     * 发送消息
     * @param tos 接收人，逗号分隔
     * @param from 发送人
     * @param type 类型，默认是1 普通消息
     * @param content 消息内容
     */
    void sendMessage(List<String> tos,String from,Integer type, String title, String secondTitle, String content, String bindTable , String dataId);

    /**
     * 发送系统消息
     * @param msgKey 消息key值
     * @param content 消息内容
     */
    void sendSystemMessage(String msgKey, String title, String secondTitle, String content);
}
