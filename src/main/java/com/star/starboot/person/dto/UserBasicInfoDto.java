package com.star.starboot.person.dto;

import com.star.starboot.person.entity.UserBasicInfo;
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
 * @Date: Created in 2020年07月02日 10:42
 */
@Data
@Accessors(chain = true)
public class UserBasicInfoDto extends UserBasicInfo {

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户代码
     */
    private String userCode;
}
