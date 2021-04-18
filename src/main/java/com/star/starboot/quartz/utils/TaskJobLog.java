package com.star.starboot.quartz.utils;

import com.star.starboot.common.utils.ContextUtils;
import com.star.starboot.quartz.entity.ScheduleJob;
import com.star.starboot.quartz.entity.ScheduleJobLog;
import com.star.starboot.quartz.service.ScheduleJobLogService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.lang.reflect.Method;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.quartz.utils
 * @Description: 定时器任务执行日志记录配置
 * @Author: xpy
 * @Date: Created in 2021年04月17日 4:04 下午
 */
public class TaskJobLog extends QuartzJobBean {

    private static final Logger LOG = LoggerFactory.getLogger(TaskJobLog.class) ;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        ScheduleJob jobBean = (ScheduleJob)context.getMergedJobDataMap().get(ScheduleJob.JOB_PARAM_KEY) ;
        ScheduleJobLogService scheduleJobLogService = (ScheduleJobLogService) ContextUtils.getBean("scheduleJobLogService") ;
        // 定时器日志记录
        ScheduleJobLog logBean = new ScheduleJobLog() ;
        logBean.setScheduleJobId(jobBean.getScheduleJobId());
        logBean.setName(jobBean.getName());
        logBean.setParams(jobBean.getParams());
        long beginTime = System.currentTimeMillis() ;
        try {
            // 加载并执行定时器的 run 方法
            Object target = ContextUtils.getBean(jobBean.getName());
            Method method = target.getClass().getDeclaredMethod("run", String.class);
            method.invoke(target, jobBean.getParams());
            long executeTime = System.currentTimeMillis() - beginTime;
            logBean.setTimes(executeTime);
            logBean.setStatus(0);
            LOG.info("定时器 === >> "+jobBean.getScheduleJobId()+"执行成功,耗时 === >> " + executeTime);
        } catch (Exception e){
            // 异常信息
            long executeTime = System.currentTimeMillis() - beginTime;
            logBean.setTimes(executeTime);
            logBean.setStatus(1);
            logBean.setError(e.getMessage());
        } finally {
            scheduleJobLogService.save(logBean) ;
        }
    }
}
