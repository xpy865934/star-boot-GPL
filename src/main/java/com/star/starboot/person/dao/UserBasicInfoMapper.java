package com.star.starboot.person.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.starboot.person.entity.UserBasicInfo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户基本信息 Mapper 接口
 * </p>
 *
 * @author xpy
 * @since 2020-07-19
 */
public interface UserBasicInfoMapper extends BaseMapper<UserBasicInfo> {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    UserBasicInfo queryById(@Param("id") String id);
}
