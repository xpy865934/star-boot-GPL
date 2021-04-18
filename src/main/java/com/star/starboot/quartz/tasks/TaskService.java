package com.star.starboot.quartz.tasks;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.quartz.tasks
 * @Description: 定时任务执行接口
 * @Author: xpy
 * @Date: Created in 2021年04月18日 11:53 上午
 */
public interface TaskService {
    void run(String params);
}
