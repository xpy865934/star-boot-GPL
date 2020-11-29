package com.star.starboot.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.starboot.system.dao.CompanyMapper;
import com.star.starboot.system.dto.CompanyDto;
import com.star.starboot.system.dto.DepartmentDto;
import com.star.starboot.system.dto.ResourcesDto;
import com.star.starboot.system.entity.Company;
import com.star.starboot.system.entity.Department;
import com.star.starboot.system.dao.DepartmentMapper;
import com.star.starboot.system.service.CompanyService;
import com.star.starboot.system.service.DepartmentService;
import liquibase.pro.packaged.D;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 组织架构 服务实现类
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private CompanyService companyService;

    @Override
    public List<CompanyDto> getDepartmentTree(String companyId) {
        List<CompanyDto> list = new ArrayList<>();
        // 查询公司列表
        Company company = companyService.getById(companyId);
        List<DepartmentDto> result = new ArrayList<>();
        if(!StringUtils.isEmpty(company)){
            // 查询所有的部门列表
            DepartmentDto dto = new DepartmentDto();
            dto.setCompanyId(companyId);
            List<DepartmentDto> departmentDtos = departmentMapper.queryList(dto);
            for (DepartmentDto departmentDto : departmentDtos) {
                List<DepartmentDto> children = findByParentDepartmentId(departmentDtos, departmentDto.getDepartmentId());
                for (DepartmentDto dept:children) {
                    dept.setParentDepartmentName(departmentDto.getDepartmentName());
                    dept.setParentDepartmentCode(departmentDto.getDepartmentCode());
                }
                departmentDto.setChildren(children);
            }
            result = departmentDtos.stream().filter(s -> StringUtils.isEmpty(s.getParentDepartmentId())).collect(Collectors.toList());
            for (DepartmentDto dept:result) {
                dept.setParentDepartmentName(company.getCompanyName());
            }
            CompanyDto companyDto = new CompanyDto();
            BeanUtils.copyProperties(company,companyDto);
            companyDto.setChildren(result);
            list.add(companyDto);
        }
        return list;
    }

    @Override
    public Department getByCodeAndCompanyCode(String departmentCode, String companyCode) {
        return departmentMapper.getByCodeAndCompanyCode(departmentCode, companyCode);
    }

    private List<DepartmentDto> findByParentDepartmentId(List<DepartmentDto> list, String parentDepartmentId){
        List<DepartmentDto> resources = list.stream().filter(s -> parentDepartmentId.equals(s.getParentDepartmentId())).collect(Collectors.toList());
        return resources;
    }
}
