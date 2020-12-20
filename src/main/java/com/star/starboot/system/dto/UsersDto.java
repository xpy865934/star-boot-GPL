package com.star.starboot.system.dto;

import com.star.starboot.system.entity.Users;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.system.dto
 * @Description: ${Description}
 * @Author: xpy
 * @Date: Created in 2020年07月02日 10:42
 */
@Data
@Accessors(chain = true)
public class UsersDto extends Users {

    /**
     * 公司ID
     */
    private String companyId;

    /**
     * 公司代码
     */
    private String companyCode;

    /**
     * 部门代码
     */
    private String departmentCode;

    /**
     * 角色
     */
    private Set<String> roles;

    /**
     * 权限
     */
    private Set<String> permissions;

    /**
     * 用户工号-公司代码   作为主键唯一
     */
    private String userCompanyCode;

    /**
     * 旧密码
     */
    private String oldPassword;
    /**
     * 部门名称
     */
    private String departmentName;
}
