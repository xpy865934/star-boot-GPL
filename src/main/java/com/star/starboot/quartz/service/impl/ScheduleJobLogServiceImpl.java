package com.star.starboot.quartz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.starboot.quartz.dao.ScheduleJobLogMapper;
import com.star.starboot.quartz.dao.ScheduleJobMapper;
import com.star.starboot.quartz.entity.ScheduleJob;
import com.star.starboot.quartz.entity.ScheduleJobLog;
import com.star.starboot.quartz.service.ScheduleJobLogService;
import com.star.starboot.quartz.service.ScheduleJobService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定时器任务日志 服务实现类
 * </p>
 *
 * @author xpy
 * @since 2020-12-28
 */
@Service
public class ScheduleJobLogServiceImpl extends ServiceImpl<ScheduleJobLogMapper, ScheduleJobLog> implements ScheduleJobLogService {

}
