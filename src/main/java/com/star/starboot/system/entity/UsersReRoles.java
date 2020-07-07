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
 * 用户和角色关联关系
 * </p>
 *
 * @author xpy
 * @since 2020-07-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_users_re_roles")
public class UsersReRoles extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户和角色的关联表
     */
    @TableId("USERS_RE_ROLES_ID")
    private String usersReRolesId;
    @TableField("USER_ID")
    private String userId;
    @TableField("ROLE_ID")
    private String roleId;


}
