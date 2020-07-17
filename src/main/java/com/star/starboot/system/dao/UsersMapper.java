package com.star.starboot.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.starboot.system.dto.UsersDto;
import com.star.starboot.system.entity.Users;
import org.apache.ibatis.annotations.Param;

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
}
