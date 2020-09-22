package com.star.starboot.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.star.starboot.system.dto.FirstDictDto;
import com.star.starboot.system.entity.FirstDict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 一级字典 服务类
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
public interface FirstDictService extends IService<FirstDict> {

    /**
     * 分页查询
     * @param firstDictDto
     * @param current
     * @param size
     * @return
     */
    IPage<FirstDictDto> queryPager(FirstDictDto firstDictDto, Integer current, Integer size);


    /**
     * 根据数据字典code获取一级字典代码
     * @param sjzdCode
     * @return
     */
    Map<String,String> getFirstDictByShzjCode(String sjzdCode);
}
