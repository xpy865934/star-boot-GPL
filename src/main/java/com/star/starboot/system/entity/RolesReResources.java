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
 * 角色和资源关联表
 * </p>
 *
 * @author xpy
 * @since 2020-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_roles_re_resources")
public class RolesReResources extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 角色资源关联表ID
     */
    @TableId("ROLES_RE_RESOURCES_ID")
    private String rolesReResourcesId;
    @TableField("RESOURCES_ID")
    private String resourcesId;
    @TableField("ROLE_ID")
    private String roleId;


}
