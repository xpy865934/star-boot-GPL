package com.star.starboot.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.star.starboot.system.dto.DictionaryDto;
import com.star.starboot.system.entity.Dictionary;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统字典表 服务类
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
public interface DictionaryService extends IService<Dictionary> {

    /**
     * 分页查询
     * @param dictionaryDto
     * @param current
     * @param size
     * @return
     */
    IPage<DictionaryDto> queryPager(DictionaryDto dictionaryDto, Integer current, Integer size);
}
