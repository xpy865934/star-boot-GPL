package com.star.starboot.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.star.starboot.system.dto.DictionaryDto;
import com.star.starboot.system.entity.Dictionary;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统字典表 Mapper 接口
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
public interface DictionaryMapper extends BaseMapper<Dictionary> {

    /**
     * 分页查询
     * @param page
     * @param dictionaryDto
     * @return
     */
    IPage<DictionaryDto> queryPage(@Param("page") Page page, @Param("dictionaryDto") DictionaryDto dictionaryDto);
}
