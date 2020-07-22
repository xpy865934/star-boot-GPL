package com.star.starboot.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.star.starboot.system.dto.UsersDto;
import com.star.starboot.system.entity.Users;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
public interface UsersService extends IService<Users> {

    /**
     * 根据公司工号和用户工号查询该用户
     * @param userCode
     * @param companyCode
     * @return
     */
    UsersDto getUserByUserCodeAndCompanyCode(String userCode, String companyCode);

    /**
     * 注册用户
     * @param usersDto
     */
    void register(UsersDto usersDto);

    /**
     * 分页查询用户信息
     * @param usersDto
     * @param current
     * @param size
     * @return
     */
    IPage<UsersDto> queryPager(UsersDto usersDto, Integer current, Integer size);
}
