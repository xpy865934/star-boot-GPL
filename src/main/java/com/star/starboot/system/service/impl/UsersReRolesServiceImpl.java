package com.star.starboot.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.starboot.system.dto.UsersReRolesDto;
import com.star.starboot.system.entity.UsersReRoles;
import com.star.starboot.system.dao.UsersReRolesMapper;
import com.star.starboot.system.service.UsersReRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户和角色关联关系 服务实现类
 * </p>
 *
 * @author xpy
 * @since 2020-07-05
 */
@Service
public class UsersReRolesServiceImpl extends ServiceImpl<UsersReRolesMapper, UsersReRoles> implements UsersReRolesService {

    @Autowired
    private UsersReRolesMapper usersReRolesMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void inserOrUpdate(UsersReRolesDto usersReRolesDto) {
        LambdaQueryWrapper<UsersReRoles> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UsersReRoles::getUserId, usersReRolesDto.getUserId());
        usersReRolesMapper.delete(wrapper);

        List<String> roles = usersReRolesDto.getRoles();
        for (String roleId: roles) {
            UsersReRoles usersReRoles = new UsersReRoles();
            usersReRoles.setUserId(usersReRolesDto.getUserId());
            usersReRoles.setRoleId(roleId);
            usersReRolesMapper.insert(usersReRoles);
        }
    }
}
