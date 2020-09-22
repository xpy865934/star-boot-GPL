package com.star.starboot.system.dto;

import com.star.starboot.system.entity.Company;
import com.star.starboot.system.entity.Department;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.system.dto
 * @Description: 数据字典dto
 * @Author: xpy
 * @Date: Created in 2020年09月19日 3:01 下午
 */
@Data
@Accessors(chain = true)
public class DepartmentDto extends Department {

    private String parentDepartmentName;

    private String parentDepartmentCode;

    private List<DepartmentDto> children;

    public String getLabel(){
        return this.getDepartmentName();
    }
}
