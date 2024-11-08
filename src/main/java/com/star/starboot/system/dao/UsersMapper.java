package com.star.starboot.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.star.starboot.system.dto.UsersDto;
import com.star.starboot.system.entity.Users;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
public interface UsersMapper extends BaseMapper<Users> {

    /**
     * 根据公司工号和用户工号查询该用户
     * @param userCode
     * @param companyCode
     * @return
     */
    UsersDto getUserByUserCodeAndCompanyCode(@Param("userCode") String userCode, @Param("companyCode") String companyCode);

    /**
     * 分页查询用户信息
     * @param page
     * @param usersDto
     * @return
     */
    IPage<UsersDto> queryPage(@Param("page") Page page, @Param("usersDto") UsersDto usersDto);

    /**
     * 查询所有用户
     * @return
     */
    List<UsersDto> queryList();

    /**
     * 根据ids获取用户
     * @param userIds
     * @return
     */
    List<UsersDto> getByIds(@Param("userIds") List<String> userIds);

    /**
     * 根据公司工号和手机号查询用户
     * @param tel
     * @param companyCode
     * @return
     */
    UsersDto getUserByUserTelAndCompanyCode(@Param("tel") String tel, @Param("companyCode") String companyCode);

    /**
     * 根据id查询
     * @param userId
     * @return
     */
    UsersDto queryById(@Param("userId") String userId);

    /**
     * 根据ids查询
     * @param ids
     * @return
     */
    List<UsersDto> listByIds(@Param("ids") List<String> ids);
}
