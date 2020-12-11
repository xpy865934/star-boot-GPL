package com.star.starboot.system.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.star.starboot.system.entity.MessageSetting;

/**
 * <p>
 * 消息配置 服务类
 * </p>
 *
 * @author xpy
 * @since 2020-12-03
 */
public interface MessageSettingService extends IService<MessageSetting> {

    /**
     * 分页获取消息配置
     * @param messageSetting
     * @param current
     * @param size
     * @return
     */
    IPage<MessageSetting> queryPager(MessageSetting messageSetting, Integer current, Integer size);
}
