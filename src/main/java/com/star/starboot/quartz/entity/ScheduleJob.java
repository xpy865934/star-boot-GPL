package com.star.starboot.quartz.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.star.starboot.common.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.quartz.jobs
 * @Description: 定时器任务
 * @Author: xpy
 * @Date: Created in 2021年04月17日 4:06 下午
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_schedule_job")
public class ScheduleJob extends AbstractEntity {


        /**
         * 任务调度参数key
         */
        @TableField(exist = false)
        public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";

        /**
         * 定时任务id
         */
        @TableId("SCHEDULE_JOB_ID")
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
         * 触发周期表达式cron
         */
        @TableField("CRON_EXPRESSION")
        private String cronExpression;
        /**
         * 状态
         */
        @TableField("STATUS")
        private Integer status;
        /**
         * 备注
         */
        @TableField("REMARK")
        private String remark;
}
