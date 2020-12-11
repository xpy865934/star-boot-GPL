package com.star.starboot.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.star.starboot.common.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 消息配置
 * </p>
 *
 * @author xpy
 * @since 2020-12-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_message_setting")
public class MessageSetting extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 消息配置ID
     */
    @TableId("MESSAGE_SETTING_ID")
    private String messageSettingId;
    /**
     * 接收人员id，逗号隔开
     */
    @TableField("MEMBER_ID")
    private String memberId;
    /**
     * 接收人员姓名
     */
    @TableField("MEMBER_NAME")
    private String memberName;
    /**
     * 名称
     */
    @TableField("NAME")
    private String name;
    /**
     * 消息key值
     */
    @TableField("KEY")
    private String key;

}
