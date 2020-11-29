package com.star.starboot.common.utils;

import ch.qos.logback.core.util.ContextUtil;
import cn.hutool.core.date.DateUtil;
import com.star.starboot.common.enums.DateType;
import com.star.starboot.constant.SystemConstant;
import com.star.starboot.system.dto.SystemBhDto;
import com.star.starboot.system.entity.SystemBh;
import com.star.starboot.system.service.SystemBhService;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.Stack;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.common.utils
 * @Description: 系统编号生成工具类
 * @Author: xpy
 * @Date: Created in 2020年11月28日 9:16 上午
 */
public class SystemBhUtils {

    private static SystemBhService systemBhService;

    private static SystemBhService getSystemBhService(){
        if (null == systemBhService){
            systemBhService = ContextUtils.getBean(SystemBhService.class);
        }
        return systemBhService;
    }

    /**
     * 单个生成编号
     * @param premix 前缀（此处即编号类型）
     * @param bhDateType 编号的时间类型
     * @param date 要求查询的此日期的最大编号，可为空
     * @param length 流水号长度
     * @return
     */
    public static String generateSingle(String premix, @Nullable DateType bhDateType, @Nullable Date date, Integer length){
        SystemBhService systemBhService = getSystemBhService();
        Integer identifier = systemBhService.getCurrentBhMax(premix, bhDateType, date,null);
        String timer =  obtainTimer(bhDateType,date);
        return String.format("%s%s%s", premix, timer, CommonUtils.fillZero(length, identifier));
    }


    private static String obtainTimer(DateType bhDateType, Date date){
        if (null == date){
            date = DateUtil.date();
        }
        String timer =  DateUtil.format(date, SystemConstant.DATE_GAPLESS_PATTERN);
        if (DateType.YEAR.equals(bhDateType)){
            timer = timer.substring(0,4);
        } else if (DateType.MONTH.equals(bhDateType)){
            timer = timer.substring(0,6);
        } else if(DateType.WEEK.equals(bhDateType)){
            timer = timer.substring(0,4) + CommonUtils.fillZero(2, DateUtil.weekOfYear(date));
        }
        return timer;
    }
}
