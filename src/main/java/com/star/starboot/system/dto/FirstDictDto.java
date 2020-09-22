package com.star.starboot.system.dto;

import com.star.starboot.system.entity.FirstDict;
import lombok.Data;
import lombok.experimental.Accessors;

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
public class FirstDictDto extends FirstDict {

    /**
     * 数据字典代码
     */
    private String dictCode;
}
