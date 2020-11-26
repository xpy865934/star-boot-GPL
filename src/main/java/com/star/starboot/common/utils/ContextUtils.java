package com.star.starboot.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.common.utils
 * @Description: 上下文Utills
 * @Author: xpy
 * @Date: Created in 2020年11月25日 8:51 下午
 */
@Component
public class ContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 设置spring上下文
     *
     * @param context spring上下文
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    /**
     * 获取容器
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取容器对象
     *
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> type) {
        return applicationContext.getBean(type);
    }
}
