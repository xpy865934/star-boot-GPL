package com.star.starboot.system.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.star.starboot.system.entity.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 消息 Mapper 接口
 * </p>
 *
 * @author xpy
 * @since 2020-12-03
 */
public interface MessageMapper extends BaseMapper<Message> {

    /**
     * 获取个人消息 分页
     * @param page
     * @param message
     * @return
     */
    IPage<Message> getUserMessagePager(@Param("page") Page page, @Param("message") Message message);

    /**
     * 不分页获取个人消息
     * @param message
     * @return
     */
    List<Message> getUserMsgList(@Param("message") Message message);

    /**
     * 获取用户未读消息数量
     * @param userId
     * @return
     */
    List<Map<String, Integer>> getMsgCount(@Param("userId") String userId);

    /**
     * 获取通知id
     * @param bindTable
     * @param dataId
     * @return
     */
    Integer getNotifyId(@Param("bindTable") String bindTable, @Param("dataId") String dataId);

    /**
     * 获取当前最大的通知id
     * @return
     */
    Integer getMaxNotifyId();
}
