package com.star.starboot.quartz.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.starboot.quartz.entity.ScheduleJobLog;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 定时器任务日志 Mapper 接口
 * </p>
 *
 * @author xpy
 * @since 2020-12-28
 */
public interface ScheduleJobLogMapper extends BaseMapper<ScheduleJobLog> {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    ScheduleJobLog queryById(@Param("id") String id);
}
