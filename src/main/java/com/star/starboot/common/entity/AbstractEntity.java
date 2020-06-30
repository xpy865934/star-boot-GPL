package com.star.starboot.common.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
    private String createAt;
    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;
    /**
     * 更新时间
     */
    @TableField("UPDATE_AT")
    private String updateAt;
    /**
     * 删除时间
     */
    @TableField("DELETED_AT")
    private String deletedAt;
    /**
     * 删除人
     */
    @TableField("DELETED_BY")
    private String deletedBy;
    /**
     * 删除标记
     */
    @TableField("DELETED_CODE")
    private String deletedCode;
}
