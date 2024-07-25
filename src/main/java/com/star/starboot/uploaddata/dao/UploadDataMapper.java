package com.star.starboot.uploaddata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.star.starboot.system.dto.UsersDto;
import com.star.starboot.uploaddata.dto.UploadDataDto;
import com.star.starboot.uploaddata.entity.UploadData;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 上报的数据 Mapper 接口
 * </p>
 *
 * @author xpy
 * @since 2020-07-19
 */
public interface UploadDataMapper extends BaseMapper<UploadData> {

    /**
     * 按照填报日期查询当月填报的数据
     * @param uploadDataDto
     * @return
     */
    UploadData queryByDate(@Param("uploadDataDto") UploadDataDto uploadDataDto, @Param("usersDto") UsersDto usersDto);

    /**
     * 分页查询
     * @param page
     * @param uploadDataDto
     * @return
     */
    IPage<UploadDataDto> queryPage(Page page, @Param("uploadDataDto") UploadDataDto uploadDataDto);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    UploadDataDto queryById(@Param("id") String id);
}
