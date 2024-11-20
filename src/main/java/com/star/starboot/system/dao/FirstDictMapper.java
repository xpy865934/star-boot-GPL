package com.star.starboot.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.star.starboot.system.dto.FirstDictDto;
import com.star.starboot.system.entity.FirstDict;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 一级字典 Mapper 接口
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
public interface FirstDictMapper extends BaseMapper<FirstDict> {

    /**
     * 分页查询
     * @param page
     * @param firstDictDto
     * @return
     */
    IPage<FirstDictDto> queryPage(@Param("page") Page page, @Param("firstDictDto") FirstDictDto firstDictDto);

    /**
     * 查询以及代码List
     * @param firstDictDto
     * @return
     */
    List<FirstDictDto> queryList(@Param("firstDictDto") FirstDictDto firstDictDto);

    /**
     * 获取所有的一级代码信息
     * @return
     */
    List<FirstDictDto> getFirstDictAll();
    /**
     * 根据id查询一级代码
     * @param id
     * @return
     */
    FirstDictDto queryById(@Param("id") String id);

    /**
     * 根据ids查询
     * @param ids
     * @return
     */
    List<FirstDictDto> listByIds(@Param("ids") List<String> ids);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    void deleteById(@Param("id") String id);
}
