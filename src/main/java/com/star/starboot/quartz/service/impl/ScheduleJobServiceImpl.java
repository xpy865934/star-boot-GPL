package com.star.starboot.quartz.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.starboot.common.utils.CommonUtils;
import com.star.starboot.common.utils.FlowableUtils;
import com.star.starboot.common.utils.ShiroUtils;
import com.star.starboot.constant.SystemConstant;
import com.star.starboot.contract.dao.ContractMapper;
import com.star.starboot.contract.dto.ContractDetailDto;
import com.star.starboot.contract.dto.ContractDto;
import com.star.starboot.contract.entity.Contract;
import com.star.starboot.contract.service.ContractDetailService;
import com.star.starboot.contract.service.ContractService;
import com.star.starboot.customer.dto.CustomerHousesDto;
import com.star.starboot.customer.service.CustomerHousesService;
import com.star.starboot.quartz.dao.ScheduleJobMapper;
import com.star.starboot.quartz.entity.ScheduleJob;
import com.star.starboot.quartz.service.ScheduleJobService;
import com.star.starboot.quartz.utils.ScheduleUtils;
import com.star.starboot.system.dto.UsersDto;
import org.apache.commons.collections.map.HashedMap;
import org.joda.time.LocalDateTime;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * <p>
 * 定时器任务 服务实现类
 * </p>
 *
 * @author xpy
 * @since 2020-12-28
 */
@Service
public class ScheduleJobServiceImpl extends ServiceImpl<ScheduleJobMapper, ScheduleJob> implements ScheduleJobService {


    @Autowired
    private Scheduler scheduler;

    @Autowired
    private ScheduleJobMapper scheduleJobMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(ScheduleJob record) {
        ScheduleUtils.createJob(scheduler,record);
        return scheduleJobMapper.insert(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateByPrimaryKeySelective(ScheduleJob record) {
        ScheduleUtils.updateJob(scheduler,record);
        return scheduleJobMapper.updateById(record);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pauseJob(String jobId) {
        ScheduleJob scheduleJob = scheduleJobMapper.selectById(jobId) ;
        ScheduleUtils.pauseJob(scheduler,jobId);
        scheduleJob.setStatus(1);
        scheduleJobMapper.updateById(scheduleJob);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resumeJob(String jobId) {
        ScheduleJob scheduleJob = scheduleJobMapper.selectById(jobId) ;
        ScheduleUtils.resumeJob(scheduler,jobId);
        scheduleJob.setStatus(0);
        scheduleJobMapper.updateById(scheduleJob);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(String jobId) {
        ScheduleJob scheduleJob = scheduleJobMapper.selectById(jobId) ;
        ScheduleUtils.run(scheduler,scheduleJob);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String jobId) {
        ScheduleUtils.deleteJob(scheduler, jobId);
        scheduleJobMapper.deleteById(jobId);
    }
}
