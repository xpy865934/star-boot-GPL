package com.star.starboot.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.starboot.common.utils.CommonUtils;
import com.star.starboot.exception.BusinessException;
import com.star.starboot.system.dao.UsersMapper;
import com.star.starboot.system.dto.UsersDto;
import com.star.starboot.system.entity.Users;
import com.star.starboot.system.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    @Override
    public void register(UsersDto usersDto) {
        // 生成秘钥和密文
        String[] encrpty = CommonUtils.encryptPassword(usersDto.getPassword());
        usersDto.setSalt(encrpty[0]);
        usersDto.setPassword(encrpty[1]);

        // 判断用户工号是否已经存在
        UsersDto user = usersMapper.getUserByUserCodeAndCompanyCode(usersDto.getUserCode(), usersDto.getCompanyCode());
        if(!StringUtils.isEmpty(user)){
            throw  new BusinessException("用户工号已经存在");
        }
        usersMapper.insert(usersDto);
    }
}
