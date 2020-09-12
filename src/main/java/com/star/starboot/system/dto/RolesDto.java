package com.star.starboot.system.dto;

import com.star.starboot.system.entity.Roles;
import com.star.starboot.system.entity.Users;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.system.dto
 * @Description: ${Description}
 * @Author: xpy
 * @Date: Created in 2020年08月17日21:18:45
 */
@Data
@Accessors(chain = true)
public class RolesDto extends Roles {

    /**
     * 公司代码
     */
    private String companyCode;

    /**
     * 公司名称
     */
    private String companyName;
}
