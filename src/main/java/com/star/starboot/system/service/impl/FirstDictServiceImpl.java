package com.star.starboot.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.starboot.system.dao.FirstDictMapper;
import com.star.starboot.system.dto.FirstDictDto;
import com.star.starboot.system.entity.FirstDict;
import com.star.starboot.system.service.FirstDictService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 一级字典 服务实现类
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
@Service
public class FirstDictServiceImpl extends ServiceImpl<FirstDictMapper, FirstDict> implements FirstDictService {

    @Autowired
    private FirstDictMapper firstDictMapper;

    @Override
    public IPage<FirstDictDto> queryPager(FirstDictDto firstDictDto, Integer current, Integer size) {
        return firstDictMapper.queryPage(new Page(current, size), firstDictDto);
    }

    @Override
    public Map<String,String> getFirstDictByShzjCode(String sjzdCode){
        Map<String,String> result = new HashedMap();
        FirstDictDto firstDictDto = new FirstDictDto();
        firstDictDto.setDictCode(sjzdCode);
        List<FirstDictDto> list = firstDictMapper.queryList(firstDictDto);
        for (FirstDictDto dto:list) {
            result.put(dto.getFirstDictCode(),dto.getFirstDictName());
        }
        return result;
    }
}
