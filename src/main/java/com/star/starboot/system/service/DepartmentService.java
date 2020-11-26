package com.star.starboot.system.service;

import com.star.starboot.system.dto.CompanyDto;
import com.star.starboot.system.entity.Company;
import com.star.starboot.system.entity.Department;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 组织架构 服务类
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
public interface DepartmentService extends IService<Department> {

    /**
     * 获取组织架构树形菜单
     * @return
     */
    List<CompanyDto> getDepartmentTree(String companyId);

    /**
     * 获取部门根据部门code和公司code
     * @param departmentCode
     * @param companyCode
     * @return
     */
    Department getByCodeAndCompanyCode(String departmentCode, String companyCode);
}
