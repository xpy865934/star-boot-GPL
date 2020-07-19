package com.star.starboot.uploaddata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.starboot.uploaddata.entity.UploadData;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

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
     * @param createAt
     * @return
     */
    UploadData queryByDate(@Param("createAt") Date createAt);
}
