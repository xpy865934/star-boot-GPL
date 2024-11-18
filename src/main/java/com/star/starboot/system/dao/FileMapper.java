package com.star.starboot.system.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.starboot.system.entity.File;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 文件上传表 Mapper 接口
 * </p>
 *
 * @author xpy
 * @since 2019-07-15
 */
public interface FileMapper extends BaseMapper<File> {

    /**
     * 查文件列表
     * @param dto
     * @return
     */
    List<File> queryList(@Param("dto") File dto);

    /**
     * 根据id查询文件
     * @param id
     * @return
     */
    List<File> queryById(@Param("id") String id);

    /**
     * 根据ids查询
     * @param ids
     * @return
     */
    List<File> listByIds(@Param("ids") List<String> ids);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    void deleteById(@Param("id") String id);
}
