package com.star.starboot.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.star.starboot.constant.SystemConstant;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 流程信息
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
@Data
@Accessors(chain = true)
public class Flow{


    private String processInstanceId;

    private String processDefinitionId;

    /**
     * bussinessKey
     */
    private String businessKey;
    /**
     * 审批意见
     */
    private String approvalComments;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 审批人
     */
    private String assignee;

    /**
     * 是否是激活的
     */
    private boolean active = false;

    /**
     * 节点名称和审批人名称
     */
    public String nodeNameAndUserName;

    /**
     * 审批时间
     */
    @JsonFormat(pattern = SystemConstant.FULL_DATE_PATTERN, timezone = "GMT+8")
    public Date endTime;
}
