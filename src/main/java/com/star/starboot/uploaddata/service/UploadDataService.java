package com.star.starboot.uploaddata.service;

import com.baomidou.mybatisplus.extension.service.IService;
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
}
