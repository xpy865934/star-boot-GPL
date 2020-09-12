package com.star.starboot.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.starboot.common.utils.ShiroUtils;
import com.star.starboot.config.shiro.UserPasswordRealm;
import com.star.starboot.system.dto.UsersDto;
import com.star.starboot.system.entity.Resources;
import com.star.starboot.system.entity.RolesReResources;
import com.star.starboot.system.dao.RolesReResourcesMapper;
import com.star.starboot.system.entity.Users;
import com.star.starboot.system.service.RolesReResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private UserPasswordRealm userPasswordRealm;

    @Override
    public List<Resources> getResourcesByRoleTid(String roleId) {
        return reResourcesMapper.getResourcesByRoleTid(roleId);
    }

    @Override
    public void save(String roleId, List<String> checked) {
        UsersDto userInfo = ShiroUtils.build().getUserInfo();
        // 删除该角色相关的资源
        reResourcesMapper.deleteByRoleId(roleId,userInfo.getUserId());

        List<RolesReResources> list = new ArrayList<>();

        // 保存新的资源
        for (int i = 0; i < checked.size(); i++) {
            RolesReResources rolesReResources = new RolesReResources();
            rolesReResources.setCreateBy(userInfo.getUserId());
            rolesReResources.setResourcesId(checked.get(i));
            rolesReResources.setRoleId(roleId);
            list.add(rolesReResources);
        }
        this.saveBatch(list);

        // 更新缓存权限
        userPasswordRealm.updateShiroUserAuthorizationInfo();
    }
}
