package com.star.starboot.system.service;

import com.star.starboot.system.dto.UsersDto;
import com.star.starboot.system.entity.Users;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

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
}
