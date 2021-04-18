package com.star.starboot.quartz.service;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.star.starboot.contract.dto.ContractDto;
import com.star.starboot.contract.entity.Contract;
import com.star.starboot.quartz.entity.ScheduleJob;

import java.util.Map;

/**
 * <p>
 * 定时器任务 服务类
 * </p>
 *
 * @author xpy
 * @since 2020-12-28
 */
public interface ScheduleJobService extends IService<ScheduleJob> {

    /**
     * 保存
     * @param record
     * @return
     */
    int insert(ScheduleJob record);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(ScheduleJob record);

    /**
     *停止
     * @param scheduleJobId
     */
    void pauseJob (String scheduleJobId) ;
    /**
     * 恢复
     * @param scheduleJobId
     */
    void resumeJob (String scheduleJobId) ;

    /**
     * 执行
     * @param scheduleJobId
     */
    void run (String scheduleJobId) ;

    /**
     * 删除
     * @param scheduleJobId
     */
    void delete (String scheduleJobId) ;
}
