package com.star.starboot.system.service;

import com.star.starboot.system.entity.Roles;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色信息 服务类
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
public interface RolesService extends IService<Roles> {

    /**
     * 获取该公司下面该用户所有的角色信息
     * @param userId
     * @param companyId
     * @return
     */
    List<Roles> findByUserIdAndCompany(String userId, String companyId);
}
