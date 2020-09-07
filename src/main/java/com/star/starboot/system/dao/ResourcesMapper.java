package com.star.starboot.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.starboot.system.dto.ResourcesDto;
import com.star.starboot.system.entity.Resources;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户菜单功能资源表 Mapper 接口
 * </p>
 *
 * @author xpy
 * @since 2020-07-02
 */
public interface ResourcesMapper extends BaseMapper<Resources> {

    /**
     * 查询资源列表
     * @param resourcesDto
     * @return
     */
    List<ResourcesDto> queryList(@Param("resourcesDto") ResourcesDto resourcesDto);
}
