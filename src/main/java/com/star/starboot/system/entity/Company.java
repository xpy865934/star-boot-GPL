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
 * 公司信息
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_company")
public class Company extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 公司ID
     */
    @TableId("COMPANY_ID")
    private String companyId;
    /**
     * 公司名称
     */
    @TableField("COMPANY_NAME")
    private String companyName;
    /**
     * 公司代码
     */
    @TableField("COMPANY_CODE")
    private String companyCode;


}
