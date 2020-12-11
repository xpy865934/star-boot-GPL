package com.star.starboot.system.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.starboot.common.enums.DateType;
import com.star.starboot.common.utils.CommonUtils;
import com.star.starboot.constant.SystemConstant;
import com.star.starboot.exception.BusinessException;
import com.star.starboot.system.dao.SystemBhMapper;
import com.star.starboot.system.entity.SystemBh;
import com.star.starboot.system.service.SystemBhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * 系统编号 服务实现类
 * </p>
 *
 * @author xpy
 * @since 2020-11-28
 */
@Service
public class SystemBhServiceImpl extends ServiceImpl<SystemBhMapper, SystemBh> implements SystemBhService {

    private static Lock lock = new ReentrantLock();

    @Autowired
    private SystemBhMapper systemBhMapper;

    @Override
    public int getCurrentBhMax(String bhType, DateType dateType, Date date, Integer increment) {
        // 初始化增量，避免重复判空
        if (null == increment){
            increment = 1;
        }
        // 根据条件获取当前最大序号
        String dateTy = buildDate(dateType, date);
        SystemBh systemBh;
        int result = 1;
        lock.lock();
        try {
            systemBh = systemBhMapper.selectOne(bhType,dateTy);
            if ( null == systemBh){
                addBhRecord(bhType,dateTy,result + increment);
            } else {
                result = systemBh.getCurrentXh();
                systemBh.setCurrentXh(result + increment);
                systemBhMapper.updateById(systemBh);
            }
            return result;
        } catch (Exception e){
            throw new BusinessException("该类编号有多种记录");
        } finally {
            lock.unlock();
        }
    }

    /**
     * 新增第一条编号记录
     * @param bhType
     * @param dateTy
     * @param init
     */
    private void addBhRecord(String bhType, String dateTy, int init) {
        SystemBh systemBh = new SystemBh();
        systemBh.setBhType(bhType);
        systemBh.setBhrqType(dateTy);
        systemBh.setCurrentXh(init);
        systemBhMapper.insert(systemBh);
    }

    /**
     * 构造时间类型
     * @param dateType
     * @param date
     * @return
     */
    private String buildDate(DateType dateType, Date date) {
        if (!StringUtils.isEmpty(dateType)){
            Calendar calendar = Calendar.getInstance();
            if (null != date){
                calendar.setTime(date);
            }
            String year = String.valueOf(calendar.get(Calendar.YEAR));
            String month = year + CommonUtils.fillZero(2,calendar.get(Calendar.MONTH)+1);
            String day = month + CommonUtils.fillZero(2,calendar.get(Calendar.DAY_OF_MONTH));
            if (dateType.equals(DateType.YEAR)){
                return year;
            } else if (dateType.equals(DateType.MONTH)){
                return month;
            } else if (dateType.equals(DateType.WEEK)){
                return year + CommonUtils.fillZero(2, DateUtil.weekOfYear(date));
            } else if (dateType.equals(DateType.DAY)){
                return day;
            }
        }
        return SystemConstant.NULL_STR;
    }
}
