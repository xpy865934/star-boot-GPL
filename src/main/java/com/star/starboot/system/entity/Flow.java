package com.star.starboot.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

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

    /**
     * bussinessKey
     */
    private String bussinessKey;
    /**
     * 审批意见
     */
    private String approvalComments;

}
