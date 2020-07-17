package com.star.starboot.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.star.starboot.constant.SystemConstant;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

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
public class AbstractEntity {

    /**
     * 流程状态
     */
    @TableField("PROCESS_STATE")
    private String processState;
    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;
    /**
     * 创建时间
     */
    @TableField("CREATE_AT")
    @JsonFormat(pattern = SystemConstant.FULL_DATE_PATTERN)
    private Date createAt;
    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;
    /**
     * 更新时间
     */
    @TableField("UPDATE_AT")
    @JsonFormat(pattern = SystemConstant.FULL_DATE_PATTERN)
    private Date updateAt;
    /**
     * 删除时间
     */
    @TableField("DELETED_AT")
    @JsonFormat(pattern = SystemConstant.FULL_DATE_PATTERN)
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
}
