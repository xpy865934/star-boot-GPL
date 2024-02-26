package com.star.starboot.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.star.starboot.system.dto.RolesDto;
import com.star.starboot.system.entity.Roles;
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

    /**
     * 分页获取角色信息
     * @param page
     * @param rolesDto
     * @return
     */
    IPage<RolesDto> queryPage(@Param("page") Page page, @Param("rolesDto") RolesDto rolesDto);

    /**
     * 根据角色id查询角色
     * @param candidateGroups
     * @return
     */
    List<RolesDto> getByIds(@Param("roleIds") List<String> candidateGroups);

    /**
     * 根据公司id获取角色信息
     * @param companyId
     * @return
     */
    List<RolesDto> getListByCompantId(@Param("companyId") String companyId);

    /**
     * 查角色列表
     * @param dto
     * @return
     */
    List<RolesDto> queryList(@Param("dto") RolesDto dto);
}
