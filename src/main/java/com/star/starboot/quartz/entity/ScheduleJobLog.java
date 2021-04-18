package com.star.starboot.quartz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.star.starboot.common.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.quartz.bean
 * @Description: 定时任务日志
 * @Author: xpy
 * @Date: Created in 2021年04月17日 4:15 下午
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_schedule_job_log")
public class ScheduleJobLog extends AbstractEntity {
        /**
         * 定时任务日志id
         */
        @TableId("SCHEDULE_JOB_LOG_ID")
        private String scheduleJobLogId;
        /**
         *
         */
        @TableField("SCHEDULE_JOB_ID")
        private String scheduleJobId;
        /**
         * 定时任务名称
         */
        @TableField("NAME")
        private String name;
        /**
         * 参数
         */
        @TableField("PARAMS")
        private String params;
        /**
         * 状态
         */
        @TableField("STATUS")
        private Integer status;
        /**
         * 错误信息
         */
        @TableField("ERROR")
        private String error;
        /**
         * 执行时长
         */
        @TableField("TIMES")
        private Long times;
}
