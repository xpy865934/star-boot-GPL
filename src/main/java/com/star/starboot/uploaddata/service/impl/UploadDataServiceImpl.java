package com.star.starboot.uploaddata.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.starboot.uploaddata.dao.UploadDataMapper;
import com.star.starboot.uploaddata.entity.UploadData;
import com.star.starboot.uploaddata.service.UploadDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 上报的数据 服务实现类
 * </p>
 *
 * @author xpy
 * @since 2020-07-19
 */
@Service
public class UploadDataServiceImpl extends ServiceImpl<UploadDataMapper, UploadData> implements UploadDataService {

    @Autowired
    private UploadDataMapper uploadDataMapper;

    @Override
    public UploadData queryByDate(Date createAt) {
        return uploadDataMapper.queryByDate(createAt);
    }
}
