package com.star.starboot.system.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.star.starboot.system.entity.MessageSetting;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 消息配置 Mapper 接口
 * </p>
 *
 * @author xpy
 * @since 2020-12-03
 */
public interface MessageSettingMapper extends BaseMapper<MessageSetting> {

    /**
     * 分页获取消息配置
     * @param page
     * @param messageSetting
     * @return
     */
    IPage<MessageSetting> queryPage(@Param("page") Page page, @Param("messageSetting") MessageSetting messageSetting);

    /**
     * 查消息列表
     * @param dto
     * @return
     */
    List<MessageSetting> queryList(@Param("dto") MessageSetting dto);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    MessageSetting queryById(@Param("id") String id);
}
