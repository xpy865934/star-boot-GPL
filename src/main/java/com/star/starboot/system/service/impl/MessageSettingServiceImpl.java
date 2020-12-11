package com.star.starboot.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.starboot.system.dao.MessageSettingMapper;
import com.star.starboot.system.entity.MessageSetting;
import com.star.starboot.system.service.MessageSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 消息配置 服务实现类
 * </p>
 *
 * @author xpy
 * @since 2020-12-03
 */
@Service
public class MessageSettingServiceImpl extends ServiceImpl<MessageSettingMapper, MessageSetting> implements MessageSettingService {

    @Autowired
    private MessageSettingMapper messageSettingMapper;


    @Override
    public IPage<MessageSetting> queryPager(MessageSetting messageSetting, Integer current, Integer size) {
        return messageSettingMapper.queryPage(new Page(current, size), messageSetting);
    }
}
