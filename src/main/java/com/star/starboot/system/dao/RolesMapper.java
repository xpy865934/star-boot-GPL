package com.star.starboot.system.dao;

import com.star.starboot.system.entity.Roles;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色信息 Mapper 接口
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
public interface RolesMapper extends BaseMapper<Roles> {

    /**
     * 获取该公司下面该用户所有的角色信息
     * @param userId
     * @param companyId
     * @return
     */
    List<Roles> findByUserIdAndCompany(@Param("userId") String userId, @Param("companyId") String companyId);
}
