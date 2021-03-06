package com.star.starboot.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.star.starboot.system.entity.SysLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统日志 服务类
 * </p>
 *
 * @author xpy
 * @since 2020-07-01
 */
public interface SysLogService extends IService<SysLog> {

    /**
     * 分页查询系统日志
     * @param sysLog
     * @param current
     * @param size
     * @return
     */
    IPage<SysLog> queryPager(SysLog sysLog, Integer current, Integer size);
}
