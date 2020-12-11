package com.star.starboot.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.star.starboot.common.enums.DateType;
import com.star.starboot.system.entity.SystemBh;

import java.util.Date;

/**
 * <p>
 * 系统编号 服务类
 * </p>
 *
 * @author xpy
 * @since 2020-11-28
 */
public interface SystemBhService extends IService<SystemBh> {

    /**
     * 获取当前最大编号的下一个编号，并将数据库的当前编号更新
     * @param bhType
     * @param dateType
     * @param date
     * @param increment
     * @return
     */
    int getCurrentBhMax(String bhType, DateType dateType, Date date, Integer increment);
}
