package com.star.starboot.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.star.starboot.constant.SystemConstant;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.common.entity
 * @Description: ${Description}
 * @Author: xpy
 * @Date: Created in 2020年06月30日 15:18
 */
@Data
@Accessors(chain = true)
public class AbstractEntity implements Serializable {


    /**
     * 流程状态  1  申请  2  审批中  3 结束
     */
    @TableField("PROCESS_STATE")
    private String processState;

    /**
     * 流程实例id
     */
    @TableField("PROCESS_INSTANCE_ID")
    private String processInstanceId;

    /**
     * 流程定义id
     */
    @TableField("PROCESS_DEFINITION_ID")
    private String processDefinitionId;

    /**
     * 流程任务id 当前任务
     */
    @TableField("TASK_IDS")
    private String taskIds;

    /**
     * 流程任务名称 当前任务
     */
    @TableField("TASK_NAMES")
    private String taskNames;

    /**
     * 上一处理人
     */
    @TableField("LAST_ASSIGNEE")
    private String lastAssignee;

    /**
     * 当前处理人id
     */
    @TableField("CURRENT_ASSIGNEE")
    private String currentAssignee;

    /**
     * 当前处理人名称
     */
    @TableField("CURRENT_ASSIGNEE_NAMES")
    private String currentAssigneeNames;

    /**
     * 当前任务keys
     */
    @TableField("TASK_KEYS")
    private String taskKeys;

    /**
     * 创建人
     */
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;
    /**
     * 创建时间
     */
    @TableField(value = "CREATE_AT", fill = FieldFill.INSERT)
    @JsonFormat(pattern = SystemConstant.FULL_DATE_PATTERN, timezone = "GMT+8")
    private Date createAt;
    /**
     * 更新人
     */
    @TableField(value = "UPDATE_BY", fill = FieldFill.UPDATE)
    private String updateBy;
    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_AT", fill = FieldFill.UPDATE)
    @JsonFormat(pattern = SystemConstant.FULL_DATE_PATTERN, timezone = "GMT+8")
    private Date updateAt;
    /**
     * 删除时间
     */
    @TableField("DELETED_AT")
    @JsonFormat(pattern = SystemConstant.FULL_DATE_PATTERN, timezone = "GMT+8")
    private Date deletedAt;
    /**
     * 删除人
     */
    @TableField("DELETED_BY")
    private String deletedBy;
    /**
     * 删除标记
     */
    @TableLogic
    @TableField("DELETED_CODE")
    private String deletedCode;

    /**
     * 流程bussinessKey
     */
    @TableField(exist = false)
    private List<String> bussinessKeys;

    /**
     * 流程状态
     */
    @TableField(exist = false)
    private String taskState;
}
