package com.star.starboot.system.service.impl;

import com.star.starboot.system.entity.Roles;
import com.star.starboot.system.dao.RolesMapper;
import com.star.starboot.system.service.RolesService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色信息 服务实现类
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
@Service
public class RolesServiceImpl extends ServiceImpl<RolesMapper, Roles> implements RolesService {

    @Autowired
    private RolesMapper rolesMapper;

    @Override
    public List<Roles> findByUserIdAndCompany(String userId, String companyId) {
        return rolesMapper.findByUserIdAndCompany(userId, companyId);
    }
}
