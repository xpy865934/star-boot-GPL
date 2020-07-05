package com.star.starboot.system.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.star.starboot.common.entity.AbstractEntity;

import com.baomidou.mybatisplus.annotations.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 组织架构
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_department")
public class Department extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 组织机构ID
     */
    @TableId("DEPARTMENT_ID")
    private String departmentId;
    /**
     * 组织机构代码
     */
    @TableField("DEPARTMENT_CODE")
    private String departmentCode;
    /**
     * 组织机构名称
     */
    @TableField("DEPARTMENT_NAME")
    private String departmentName;
    /**
     * 父级组织机构ID 为空表示是第一级组织机构
     */
    @TableField("PARENT_DEPARTMENT_ID")
    private String parentDepartmentId;
    @TableField("COMPANY_ID")
    private String companyId;


}
