package com.star.starboot.annotation;

import java.lang.annotation.*;

/**
 * All rights Reserved, Designed By www.xpyvip.top
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
}
