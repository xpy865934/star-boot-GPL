package com.star.starboot.uploaddata.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.star.starboot.uploaddata.dto.UploadDataDto;
import com.star.starboot.uploaddata.entity.UploadData;

import java.util.Date;

/**
 * <p>
 * 上报的数据 服务类
 * </p>
 *
 * @author xpy
 * @since 2020-07-19
 */
public interface UploadDataService extends IService<UploadData> {

    /**
     * 根据填写日期查询填报记录
     * @param createAt
     * @return
     */
    UploadData queryByDate(Date createAt);

    /**
     * 分页查询
     * @param uploadDataDto
     * @param current
     * @param size
     * @return
     */
    IPage<UploadDataDto> queryPager(UploadDataDto uploadDataDto, Integer current, Integer size);
}
