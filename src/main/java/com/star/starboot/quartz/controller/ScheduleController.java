package com.star.starboot.quartz.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.star.starboot.annotation.SysLog;
import com.star.starboot.common.controller.AbstractController;
import com.star.starboot.common.utils.FlowableUtils;
import com.star.starboot.common.vo.Result;
import com.star.starboot.constant.SystemConstant;
import com.star.starboot.contract.dto.ContractDto;
import com.star.starboot.contract.entity.Contract;
import com.star.starboot.contract.service.ContractService;
import com.star.starboot.controllerAssert.ContractCommitAssert;
import com.star.starboot.quartz.entity.ScheduleJob;
import com.star.starboot.quartz.service.ScheduleJobService;
import com.star.starboot.system.entity.Flow;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 定时任务 前端控制器
 * </p>
 *
 * @author xpy
 * @since 2020-12-28
 */
@RestController
@RequestMapping("/schedule")
@CrossOrigin
@Slf4j
public class ScheduleController extends AbstractController {

    @Autowired
    private ScheduleJobService scheduleJobService;

    /**
     * 添加定时器
     */
    @RequestMapping("/save")
    public void save (){
        ScheduleJob scheduleJob = new ScheduleJob() ;
        scheduleJob.setName("TestTask");
        // 每分钟执行一次
        scheduleJob.setCronExpression("0 0/1 * * * ?");
        scheduleJob.setParams("Hello,Quart-Job");
        scheduleJob.setStatus(0);
        scheduleJob.setRemark("获取时间定时器");
        scheduleJobService.insert(scheduleJob) ;
    }

    /**
     * 执行一次定时器
     */
    @RequestMapping("/runJob")
    public String runJob (){
        String jobId = "";
        scheduleJobService.run(jobId);
        return "success" ;
    }

    /**
     * 更新定时器
     */
    @RequestMapping("/updateJob")
    public String updateJob (){
        String jobId = "";
        ScheduleJob scheduleJob = scheduleJobService.getById(jobId) ;
        scheduleJob.setParams("Hello,Job_Quart");
        scheduleJobService.updateByPrimaryKeySelective(scheduleJob) ;
        return "success" ;
    }

    /**
     * 停止定时器
     */
    @RequestMapping("/pauseJob")
    public String pauseJob (){
        String jobId = "";
        scheduleJobService.pauseJob(jobId);
        return "success" ;
    }

    /**
     * 恢复定时器
     */
    @RequestMapping("/resumeJob")
    public String resumeJob (){
        String jobId = "";
        scheduleJobService.resumeJob(jobId);
        return "success" ;
    }

    /**
     * 删除定时器
     */
    @RequestMapping("/deleteJob")
    public String deleteJob (){
        String jobId = "";
        scheduleJobService.delete(jobId);
        return "success" ;
    }
}

