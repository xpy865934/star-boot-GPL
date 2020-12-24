package com.star.starboot.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.star.starboot.common.entity.AbstractEntity;
import com.star.starboot.constant.SystemConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 消息
 * </p>
 *
 * @author xpy
 * @since 2020-12-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_message")
public class Message extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 消息id
     */
    @TableId("MESSAGE_ID")
    private String messageId;
    /**
     * 接收人id
     */
    @TableField("MEMBER_ID")
    private String memberId;
    /**
     * 消息类型 1 普通消息 2 流程消息
     */
    @TableField("TYPE")
    private Integer type;
    /**
     * 是否已读  1 已读 0 未读
     */
    @TableField("MESSAGE_READ")
    private Integer messageRead;
    /**
     * 阅读时间
     */
    @TableField("READ_TIME")
    @JsonFormat(pattern = SystemConstant.FULL_DATE_PATTERN, timezone = "GMT+8")
    private Date readTime;
    /**
     * APP消息内容
     */
    @TableField("APP_MESSAGE")
    private String appMessage;
    @TableField("SYS_MESSAGE")
    private String sysMessage;
    /**
     * 邮件消息内容
     */
    @TableField("EMAIL_MESSAGE")
    private String emailMessage;
    /**
     * 发送人id
     */
    @TableField("FROM_ID")
    private String fromId;
    /**
     * 标题
     */
    @TableField("TITLE")
    private String title;
    /**
     * 二级标题
     */
    @TableField("SECOND_TITLE")
    private String secondTitle;
    /**
     * 绑定表
     */
    @TableField("BIND_TABLE")
    private String bindTable;
    /**
     * 数据id
     */
    @TableField("DATA_ID")
    private String dataId;
    /**
     * 通知ID
     */
    @TableField("NOTIFY_ID")
    private Integer notifyId;

}
