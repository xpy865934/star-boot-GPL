package com.star.starboot.system.dto;

import com.star.starboot.system.entity.UsersReRoles;
import lombok.Data;

import java.util.List;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.system.dto
 * @Description: 用户角色关联Dto
 * @Author: xpy
 * @Date: Created in 2020年12月22日 3:26 下午
 */
@Data
public class UsersReRolesDto extends UsersReRoles {

    private List<String> roles;
}
