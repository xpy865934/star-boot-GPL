package com.star.starboot.system.dao;

import com.star.starboot.system.entity.Resources;
import com.star.starboot.system.entity.RolesReResources;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色和资源关联表 Mapper 接口
 * </p>
 *
 * @author xpy
 * @since 2020-07-02
 */
public interface RolesReResourcesMapper extends BaseMapper<RolesReResources> {

    /**
     * 获取该角色下面所有的资源信息
     * @param roleId
     * @return
     */
    List<Resources> getResourcesByRoleTid(@Param("roleId") String roleId);
}
