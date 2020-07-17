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
 * 角色信息
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_roles")
public class Roles extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @TableId("ROLE_ID")
    private String roleId;
    /**
     * 角色代码
     */
    @TableField("ROLE_CODE")
    private String roleCode;
    /**
     * 角色名称
     */
    @TableField("ROLE_NAME")
    private String roleName;
    /**
     * 公司ID
     */
    @TableField("COMPANY_ID")
    private String companyId;


}
