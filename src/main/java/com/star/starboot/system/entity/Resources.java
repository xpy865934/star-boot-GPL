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
 * 用户菜单功能资源表
 * </p>
 *
 * @author xpy
 * @since 2020-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_resources")
public class Resources extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 资源ID
     */
    @TableId("RESOURCES_ID")
    private String resourcesId;
    /**
     * 资源编号
     */
    @TableField("RESOURCES_NUM")
    private Integer resourcesNum;
    /**
     * 资源名称
     */
    @TableField("RESOURCES_NAME")
    private String resourcesName;
    /**
     * 资源类型 1 菜单 2 按钮
     */
    @TableField("RESOURCES_TYPE")
    private Integer resourcesType;
    /**
     * 资源图标
     */
    @TableField("RESOURCES_IMG")
    private String resourcesImg;
    /**
     * 资源说明
     */
    @TableField("RESOURCES_DS")
    private String resourcesDs;
    /**
     * 超级管理员独有标记 1 超级管理员 0 否 
     */
    @TableField("RESOURCES_MANA")
    private Integer resourcesMana;
    /**
     * 父级资源编号
     */
    @TableField("PARENT_RESOURCES_NUM")
    private Integer parentResourcesNum;
    /**
     * 资源代码
     */
    @TableField("RESOURCES_CODE")
    private String resourcesCode;
    @TableField("COMPANY_ID")
    private String CompanyId;
    /**
     * 排序号
     */
    @TableField("pxh")
    private String pxh;
    /**
     * 是否启用 1 启用 0 未启用 默认是1
     */
    @TableField("used")
    private Integer used;
}
