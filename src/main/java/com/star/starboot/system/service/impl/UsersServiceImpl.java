package com.star.starboot.system.service.impl;

import com.star.starboot.system.dto.UsersDto;
import com.star.starboot.system.entity.Users;
import com.star.starboot.system.dao.UsersMapper;
import com.star.starboot.system.service.UsersService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public UsersDto getUserByUserCodeAndCompanyCode(String userCode, String companyCode) {
        return usersMapper.getUserByUserCodeAndCompanyCode(userCode, companyCode);
    }
}
