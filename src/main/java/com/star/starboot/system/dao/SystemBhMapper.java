package com.star.starboot.system.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.starboot.system.entity.SystemBh;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统编号 Mapper 接口
 * </p>
 *
 * @author xpy
 * @since 2020-11-28
 */
public interface SystemBhMapper extends BaseMapper<SystemBh> {

    /**
     * 查询当前编号
     * @param bhType
     * @param dateTy
     * @return
     */
    SystemBh selectOne(@Param("bhType") String bhType, @Param("dateTy") String dateTy);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    SystemBh queryById(@Param("id") String id);
}
