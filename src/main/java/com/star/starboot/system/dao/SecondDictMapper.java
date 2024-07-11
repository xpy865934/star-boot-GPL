package com.star.starboot.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.starboot.system.entity.SecondDict;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 二级字典 Mapper 接口
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
public interface SecondDictMapper extends BaseMapper<SecondDict> {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    SecondDict queryById(@Param("id") String id);
}
