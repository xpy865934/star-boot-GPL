package com.star.starboot.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.starboot.system.dto.CompanyDto;
import com.star.starboot.system.entity.Company;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 根据id查询
     * @param id
     * @return
     */
    CompanyDto queryById(@Param("id") String id);

    /**
     * 根据ids查询
     * @param ids
     * @return
     */
    List<CompanyDto> listByIds(@Param("ids") List<String> ids);
}
