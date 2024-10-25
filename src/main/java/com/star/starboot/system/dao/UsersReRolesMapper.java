package com.star.starboot.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.starboot.system.dto.UsersReRolesDto;
import com.star.starboot.system.entity.UsersReRoles;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户和角色关联关系 Mapper 接口
 * </p>
 *
 * @author xpy
 * @since 2020-07-05
 */
public interface UsersReRolesMapper extends BaseMapper<UsersReRoles> {

    /**
     * 查用户和角色关联关系列表
     * @param dto
     * @return
     */
    List<UsersReRolesDto> queryList(@Param("dto") UsersReRolesDto dto);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    UsersReRolesDto queryById(@Param("id") String id);

    /**
     * 根据ids查询
     * @param ids
     * @return
     */
    List<UsersReRolesDto> listByIds(@Param("ids") List<String> ids);
}
