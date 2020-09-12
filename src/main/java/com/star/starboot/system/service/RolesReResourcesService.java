package com.star.starboot.system.service;

import com.star.starboot.system.entity.Resources;
import com.star.starboot.system.entity.RolesReResources;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色和资源关联表 服务类
 * </p>
 *
 * @author xpy
 * @since 2020-07-02
 */
public interface RolesReResourcesService extends IService<RolesReResources> {

    /**
     * 获取该角色下面所有的资源信息
     * @param roleId
     * @return
     */
    List<Resources> getResourcesByRoleTid(String roleId);

    /**
     * 保存角色和资源的关联关系
     * @param roleId
     * @param checked
     */
    void save(String roleId, List<String> checked);
}
