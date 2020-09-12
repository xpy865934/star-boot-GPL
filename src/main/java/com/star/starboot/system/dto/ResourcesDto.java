package com.star.starboot.system.dto;

import com.star.starboot.system.entity.Resources;
import com.star.starboot.system.entity.Users;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

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
public class ResourcesDto extends Resources {

    /**
     * 子节点
     */
    private List<ResourcesDto> children;

    /**
     * 父节点资源名称
     */
    private String parentResourcesName;
}
