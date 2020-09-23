package com.star.starboot.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.starboot.system.dao.DictionaryMapper;
import com.star.starboot.system.dto.DictionaryDto;
import com.star.starboot.system.entity.Dictionary;
import com.star.starboot.system.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统字典表 服务实现类
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements DictionaryService {

    @Autowired
    private DictionaryMapper dictionaryMapper;

    @Override
    public IPage<DictionaryDto> queryPager(DictionaryDto dictionaryDto, Integer current, Integer size) {
        return dictionaryMapper.queryPage(new Page(current, size), dictionaryDto);

    }
}
