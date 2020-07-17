package com.star.starboot.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.starboot.system.entity.Resources;
import com.star.starboot.system.entity.RolesReResources;
import com.star.starboot.system.dao.RolesReResourcesMapper;
import com.star.starboot.system.service.RolesReResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色和资源关联表 服务实现类
 * </p>
 *
 * @author xpy
 * @since 2020-07-02
 */
@Service
public class RolesReResourcesServiceImpl extends ServiceImpl<RolesReResourcesMapper, RolesReResources> implements RolesReResourcesService {

    @Autowired
    private RolesReResourcesMapper reResourcesMapper;

    @Override
    public List<Resources> getResourcesByRoleTid(String roleId) {
        return reResourcesMapper.getResourcesByRoleTid(roleId);
    }
}
