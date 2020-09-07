package com.star.starboot.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.star.starboot.system.dto.ResourcesDto;
import com.star.starboot.system.entity.Resources;

import java.util.List;

/**
 * <p>
 * 用户菜单功能资源表 服务类
 * </p>
 *
 * @author xpy
 * @since 2020-07-02
 */
public interface ResourcesService extends IService<Resources> {

    /**
     * 查询资源列表
     * @param resourcesDto
     * @return
     */
    List<ResourcesDto> queryList(ResourcesDto resourcesDto);
}
