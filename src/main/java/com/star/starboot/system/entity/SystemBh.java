package com.star.starboot.system.entity;

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
 * <p>
 * 系统编号
 * </p>
 *
 * @author xpy
 * @since 2020-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_system_bh")
public class SystemBh extends AbstractEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 系统编号id
     */
    @TableId("SYSTEM_BH_ID")
    private String systemBhId;
    /**
     * 编号类型
     */
    @TableField("BH_TYPE")
    private String bhType;
    /**
     * 编号日期类型
     */
    @TableField("BHRQ_TYPE")
    private String bhrqType;
    /**
     * 当前序号
     */
    @TableField("CURRENT_XH")
    private Integer currentXh;
}
