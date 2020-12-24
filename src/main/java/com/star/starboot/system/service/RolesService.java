package com.star.starboot.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.star.starboot.system.dto.RolesDto;
import com.star.starboot.system.entity.Roles;
import com.baomidou.mybatisplus.extension.service.IService;
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

    /**
     * 分页获取角色信息
     * @param rolesDto
     * @param current
     * @param size
     * @return
     */
    IPage<RolesDto> queryPager(RolesDto rolesDto, Integer current, Integer size);

    /**
     * 根据角色id查询角色
     * @param candidateGroups
     * @return
     */
    List<RolesDto> getByIds(List<String> candidateGroups);

    /**
     * 获取公司下面的角色信息
     * @param companyId
     * @return
     */
    List<RolesDto> getListByCompantId(String companyId);
}
