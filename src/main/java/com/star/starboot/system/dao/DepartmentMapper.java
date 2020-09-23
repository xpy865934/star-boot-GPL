package com.star.starboot.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.starboot.system.dto.DepartmentDto;
import com.star.starboot.system.entity.Department;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 组织架构 Mapper 接口
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    /**
     * 根据部门代码和公司代码获取部门信息
     * @param departmentCode
     * @param companyCode
     * @return
     */
    Department getByCodeAndCompanyCode(@Param("departmentCode") String departmentCode, @Param("companyCode") String companyCode);

    /**
     * 查询部门列表
     * @param dto
     * @return
     */
    List<DepartmentDto> queryList(@Param("dto") DepartmentDto dto);
}
