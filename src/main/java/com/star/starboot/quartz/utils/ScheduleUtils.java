package com.star.starboot.quartz.utils;

import com.star.starboot.quartz.entity.ScheduleJob;
import org.quartz.*;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.quartz.utils
 * @Description: 定时任务工具类
 * @Author: xpy
 * @Date: Created in 2021年04月17日 3:56 下午
 */
public class ScheduleUtils {

        private ScheduleUtils (){}

        private static final String SCHEDULE_NAME = "STAR_BOOT_" ;

        /**
         * 触发器 KEY
         */
        public static TriggerKey getTriggerKey(String jobId){
            return TriggerKey.triggerKey(SCHEDULE_NAME+jobId) ;
        }

        /**
         * 定时器 Key
         */
        public static JobKey getJobKey (String jobId){
            return JobKey.jobKey(SCHEDULE_NAME+jobId) ;
        }

        /**
         * 表达式触发器
         */
        public static CronTrigger getCronTrigger (Scheduler scheduler,String jobId){
            try {
                return (CronTrigger)scheduler.getTrigger(getTriggerKey(jobId)) ;
            } catch (SchedulerException e){
                throw new RuntimeException("getCronTrigger Fail",e) ;
            }
        }

        /**
         * 创建定时器
         */
        public static void createJob (Scheduler scheduler, ScheduleJob scheduleJob){
            try {
                // 构建定时器
                JobDetail jobDetail = JobBuilder.newJob(TaskJobLog.class).withIdentity(getJobKey(scheduleJob.getScheduleJobId())).build() ;
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                        .cronSchedule(scheduleJob.getCronExpression())
                        .withMisfireHandlingInstructionDoNothing() ;
                CronTrigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity(getTriggerKey(scheduleJob.getScheduleJobId()))
                        .withSchedule(scheduleBuilder).build() ;
                jobDetail.getJobDataMap().put(ScheduleJob.JOB_PARAM_KEY,scheduleJob);
                scheduler.scheduleJob(jobDetail,trigger) ;
                // 如果该定时器处于暂停状态
                if (scheduleJob.getStatus() == 1){
                    pauseJob(scheduler,scheduleJob.getScheduleJobId()) ;
                }
            } catch (SchedulerException e){
                throw new RuntimeException("createJob Fail",e) ;
            }
        }

        /**
         * 更新定时任务
         */
        public static void updateJob(Scheduler scheduler, ScheduleJob scheduleJob) {
            try {
                // 构建定时器
                TriggerKey triggerKey = getTriggerKey(scheduleJob.getScheduleJobId());
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression())
                        .withMisfireHandlingInstructionDoNothing();
                CronTrigger trigger = getCronTrigger(scheduler, scheduleJob.getScheduleJobId());
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
                trigger.getJobDataMap().put(ScheduleJob.JOB_PARAM_KEY, scheduleJob);
                scheduler.rescheduleJob(triggerKey, trigger);
                // 如果该定时器处于暂停状态
                if(scheduleJob.getStatus() == 1){
                    pauseJob(scheduler, scheduleJob.getScheduleJobId());
                }
            } catch (SchedulerException e) {
                throw new RuntimeException("updateJob Fail",e) ;
            }
        }

        /**
         * 停止定时器
         */
        public static void pauseJob (Scheduler scheduler,String jobId){
            try {
                scheduler.pauseJob(getJobKey(jobId));
            } catch (SchedulerException e){
                throw new RuntimeException("pauseJob Fail",e) ;
            }
        }

        /**
         * 恢复定时器
         */
        public static void resumeJob (Scheduler scheduler,String jobId){
            try {
                scheduler.resumeJob(getJobKey(jobId));
            } catch (SchedulerException e){
                throw new RuntimeException("resumeJob Fail",e) ;
            }
        }

        /**
         * 删除定时器
         */
        public static void deleteJob (Scheduler scheduler,String jobId){
            try {
                scheduler.deleteJob(getJobKey(jobId));
            } catch (SchedulerException e){
                throw new RuntimeException("deleteJob Fail",e) ;
            }
        }

        /**
         * 执行定时器
         */
        public static void run (Scheduler scheduler, ScheduleJob scheduleJob){
            try {
                JobDataMap dataMap = new JobDataMap() ;
                dataMap.put(ScheduleJob.JOB_PARAM_KEY,scheduleJob);
                scheduler.triggerJob(getJobKey(scheduleJob.getScheduleJobId()),dataMap);
            } catch (SchedulerException e){
                throw new RuntimeException("run Fail",e) ;
            }
        }
}
