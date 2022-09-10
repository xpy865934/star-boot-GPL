package com.star.starboot.system.controller;


import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.star.starboot.annotation.SysLog;
import com.star.starboot.common.controller.AbstractController;
import com.star.starboot.common.vo.Result;
import com.star.starboot.exception.BusinessException;
import com.star.starboot.system.service.SysLogService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 系统日志 前端控制器
 * </p>
 *
 * @author xpy
 * @since 2020-07-01
 */
@RestController
@RequestMapping("/sysLog")
@CrossOrigin
@Slf4j
public class SysLogController extends AbstractController {

    @Autowired
    private SysLogService sysLogService;


    /**
     * 分页获取系统日志信息
     * @return
     */
    @ApiOperation(value = "系统日志-分页获取系统日志信息", notes = "系统日志-分页获取系统日志信息")
    @PostMapping("/queryPager")
    @RequiresPermissions("sysLog_queryPager")
    @SysLog(description = "系统日志-分页获取系统日志信息")
    public Result queryPager(@RequestBody JSONObject param){
        Integer current = param.getInteger("current");
        Integer size = param.getInteger("size");
        com.star.starboot.system.entity.SysLog sysLog = param.getObject("bean", com.star.starboot.system.entity.SysLog.class);
        IPage<com.star.starboot.system.entity.SysLog> list = sysLogService.queryPager(sysLog,current,size);
        return Result.success(list);
    }

    /**
     * 系统日志实时刷新
     * @param lines
     * @return
     */
    @ApiOperation(value = "系统日志-系统日志实时刷新", notes = "系统日志-系统日志实时刷新")
    @GetMapping("/getLogFiles")
    @RequiresPermissions("sysLog_query")
    @SysLog(description = "系统日志-系统日志实时刷新")
    public Result getLogFiles(Integer lines){
        String logPath  = "";
        String logFile = "nohup.out";
        if(ObjectUtil.isEmpty(lines)){
            throw new BusinessException("行数不能为空");
        }
        File file = new File(logPath, logFile);
        List<String> linesArray = new ArrayList<>();

        final StringBuilder result = new StringBuilder();

        if (!file.exists()) {
            return Result.success(StringUtils.EMPTY);
        }
        long count = 0;

        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            long length = randomAccessFile.length();
            if (length == 0L) {
                return Result.success(StringUtils.EMPTY);
            } else {
                long pos = length - 1;
                while (pos > 0) {
                    pos--;
                    randomAccessFile.seek(pos);
                    if (randomAccessFile.readByte() == '\n') {
                        String line = randomAccessFile.readLine();
                        linesArray.add(new String(line.getBytes(StandardCharsets.ISO_8859_1),
                                StandardCharsets.UTF_8));
                        count++;
                        if (count == lines) {
                            break;
                        }
                    }
                }
                if (pos == 0) {
                    randomAccessFile.seek(0);
                    linesArray.add(new String(
                            randomAccessFile.readLine().getBytes(StandardCharsets.ISO_8859_1),
                            StandardCharsets.UTF_8));
                }
            }
        } catch (Exception e) {
            throw new BusinessException("读取日志失败");
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Collections.reverse(linesArray);
        linesArray.forEach(line -> result.append(line).append(StringUtils.LF));
        return Result.success(result.toString());
    }
}

