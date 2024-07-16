package com.star.starboot.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.star.starboot.system.entity.SysLog;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统日志 Mapper 接口
 * </p>
 *
 * @author xpy
 * @since 2020-07-01
 */
public interface SysLogMapper extends BaseMapper<SysLog> {

    /**
     * 分页查询
     * @param page
     * @param sysLog
     * @return
     */
    IPage<SysLog> queryPage(@Param("page") Page page, @Param("sysLog") SysLog sysLog);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    SysLog queryById(@Param("id") String id);
}
