package com.star.starboot.annotation;

import java.lang.annotation.*;

/**
 * All rights Reserved, Designed By www.monkey.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.annotation
 * @Description: 记录系统操作日志
 * @Author: xpy
 * @Date: Created in 2020年07月01日 10:44
 */
// 作用在方法和参数上面
@Target({ElementType.PARAMETER,ElementType.METHOD})
// 运行时注解
@Retention(RetentionPolicy.RUNTIME)
// 表明该被javadoc工具记录
@Documented
public @interface SysLog {
    String description() default "";

    /**
     * 模块名称
     */
    String module() default "";

    /**
     * 日志类型 1 登录日志 2 操作日志 3 定时任务
     */
    int logType() default 2;

    /**
     * 日志操作类型 1 查询 2 添加 3 更新 4 删除 5 导入 6 导出
     */
    int logAction() default 1;
}
