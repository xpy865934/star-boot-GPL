package com.star.starboot.system.dto;

import com.star.starboot.common.enums.DateType;
import com.star.starboot.system.entity.SystemBh;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.system.dto
 * @Description: 数据字典dto
 * @Author: xpy
 * @Date: Created in 2020年09月19日 3:01 下午
 */
@Data
@Accessors(chain = true)
public class SystemBhDto extends SystemBh {

    /**
     * 前缀
     */
    private String premix;
    /**
     * 日期类型
     */
    private DateType dateType;
    /**
     * 日期
     */
    private Date date;
    /**
     * 流水号长度
     */
    private Integer bhLength;
}
