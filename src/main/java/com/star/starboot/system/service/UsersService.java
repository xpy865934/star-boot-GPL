package com.star.starboot.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.star.starboot.system.dto.UsersDto;
import com.star.starboot.system.entity.Users;

import java.util.List;

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

    /**
     * 删除用户
     * @param userId
     */
    void deleteById(String userId);

    /**
     * 查询所有用户
     * @return
     */
    List<UsersDto> queryList();

    /**
     * 根据用户ids获取用户信息
     * @param userIds
     * @return
     */
    List<UsersDto> getByIds(List<String> userIds);

    /**
     * 根据公司工号和手机号查询用户
     * @param tel
     * @param companyCode
     * @return
     */
    UsersDto getUserByUserTelAndCompanyCode(String tel, String companyCode);
}
