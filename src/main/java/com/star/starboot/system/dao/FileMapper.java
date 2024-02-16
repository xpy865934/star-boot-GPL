package com.star.starboot.system.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.starboot.system.entity.File;

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
}
