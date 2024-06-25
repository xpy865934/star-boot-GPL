package com.star.starboot.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.starboot.system.entity.Resources;
import com.star.starboot.system.entity.RolesReResources;
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

    /**
     * 删除该角色下的所有资源信息
     * @param roleId
     * @param userId
     */
    void deleteByRoleId(@Param("roleId") String roleId, @Param("userId") String userId);


    /**
     * 查询列表
     * @param dto
     * @return
     */
    List<RolesReResources> queryList(@Param("dto") RolesReResources dto);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    RolesReResources queryById(@Param("id") String id);
}
