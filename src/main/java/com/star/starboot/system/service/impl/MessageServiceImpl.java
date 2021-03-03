package com.star.starboot.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.starboot.common.utils.ShiroUtils;
import com.star.starboot.constant.SystemConstant;
import com.star.starboot.system.dao.MessageMapper;
import com.star.starboot.system.dto.UsersDto;
import com.star.starboot.system.entity.Message;
import com.star.starboot.system.service.MessageSend;
import com.star.starboot.system.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * <p>
 * 消息 服务实现类
 * </p>
 *
 * @author xpy
 * @since 2020-12-03
 */
@Service
@Slf4j
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private Map<String, MessageSend> sendMap;

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public IPage<Message> getUserMessagePager(Message message, Integer current, Integer size) {
        UsersDto usersDto = ShiroUtils.build().getUserInfo();
        message.setMemberId(usersDto.getUserId());
        return messageMapper.getUserMessagePager(new Page(current, size), message);
    }

    @Override
    public List<Message> getUserMsgList(Message message) {
        UsersDto usersDto = ShiroUtils.build().getUserInfo();
        message.setMemberId(usersDto.getUserId());
        List<Message> list = messageMapper.getUserMsgList(message);
        return list;
    }

    @Override
    public void upadteUserMessageRead(Message message) {
        Message msg = new Message();
        msg.setMessageId(message.getMessageId());
        msg.setReadTime(new Date());
        msg.setMessageRead(1);
        messageMapper.updateById(msg);
    }

    @Override
    public void updateAllRead(Message message) {
        UsersDto usersDto = ShiroUtils.build().getUserInfo();
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Message::getMemberId, usersDto.getUserId());
        Message msg = new Message();
        msg.setReadTime(new Date());
        msg.setMessageRead(1);
        messageMapper.update(msg, wrapper);
    }

    @Override
    public List<Map<String, Integer>> getMsgCount() {
        UsersDto usersDto = ShiroUtils.build().getUserInfo();
        return messageMapper.getMsgCount(usersDto.getUserId());
    }

    @Override
    public void sendMessage(List<String> tos, String from, Integer type, String title, String secondTitle, String content, String bindTable, String dataId, String processKey) {
        // 构建消息
        List<Message> list = new ArrayList<>();
        if(!StringUtils.isEmpty(tos) && tos.size()>0){
            // 查询uni push是否需要覆盖消息
            Integer notifyId = null;
            if(!StringUtils.isEmpty(bindTable)){
                notifyId = messageMapper.getNotifyId(bindTable,dataId);
                if(StringUtils.isEmpty(notifyId)){
                    notifyId = messageMapper.getMaxNotifyId();
                }
            }
            for (String t:tos) {
                Message message = new Message();
                // 设置唯一性UUID
                message.setTitle(title);
                message.setSecondTitle(secondTitle);
                message.setMemberId(t);
                if(StringUtils.isEmpty(type)){
                    // 普通消息
                    type = SystemConstant.NORMALMSG;
                }
                message.setType(type);
                message.setAppMessage(content);
                message.setSysMessage(content);
                message.setEmailMessage(content);
                message.setFromId(from);
                message.setBindTable(bindTable);
                message.setDataId(dataId);
                message.setNotifyId(notifyId);
                message.setProcessKey(processKey);
                list.add(message);
            }

            if(list.size()>0){
                for (int i = 0; i < list.size(); i++) {
                    messageMapper.insert(list.get(i));
                }

                // 发送消息
                Message message = new Message();
                message.setTitle(title);
                message.setSecondTitle(secondTitle);
                message.setAppMessage(content);
                message.setSysMessage(content);
                message.setEmailMessage(content);
                message.setBindTable(bindTable);
                message.setDataId(dataId);
                message.setNotifyId(notifyId);
                message.setProcessKey(processKey);
                this.send(SystemConstant.MSG_SYSTEM, tos, from, message);
                this.send(SystemConstant.MSG_APP, tos, from, message);
                this.send(SystemConstant.MSG_EMAIL, tos, from, message);
            }
        }
    }

    @Override
    public void sendSystemMessage(String msgKey, String title, String secondTitle, String content) {

    }

    private void send(String msgType, List<String> tos, String from, Message message){
        MessageSend messageSend = sendMap.get(msgType);
        if(StringUtils.isEmpty(messageSend)){
            log.info(msgType+"不支持");
            return;
        }
        messageSend.sendMsg(tos,message,from);
    }
}
