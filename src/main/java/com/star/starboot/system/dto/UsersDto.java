package com.star.starboot.system.dto;

import com.star.starboot.system.entity.Users;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
}
