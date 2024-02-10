package com.star.starboot.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.starboot.system.dto.CompanyDto;
import com.star.starboot.system.entity.Company;

/**
 * <p>
 * 公司信息 Mapper 接口
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
public interface CompanyMapper extends BaseMapper<Company> {

    /**
     * 查询公司列表
     * @param dto
     * @return
     */
    List<CompanyDto> queryList(@Param("dto") CompanyDto dto);
}
