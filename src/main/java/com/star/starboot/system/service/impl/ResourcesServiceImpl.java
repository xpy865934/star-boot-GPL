package com.star.starboot.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.starboot.system.dto.ResourcesDto;
import com.star.starboot.system.entity.Resources;
import com.star.starboot.system.dao.ResourcesMapper;
import com.star.starboot.system.service.ResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户菜单功能资源表 服务实现类
 * </p>
 *
 * @author xpy
 * @since 2020-07-02
 */
@Service
public class ResourcesServiceImpl extends ServiceImpl<ResourcesMapper, Resources> implements ResourcesService {

    @Autowired
    private ResourcesMapper resourcesMapper;

    @Override
    public List<ResourcesDto> queryList(ResourcesDto resourcesDto) {
        // 1.先查询出所有数据
        // 2.将每组数据的children都根据父节点查询并设置
        // 3.过滤出父级菜单为null的资源
        List<ResourcesDto> result = new ArrayList<>();
        List<ResourcesDto> resources = resourcesMapper.queryList(resourcesDto);
        for (ResourcesDto resource : resources) {
            List<ResourcesDto> children = findByParentResourcesNum(resources, resource.getResourcesNum());
            resource.setChildren(children);
        }
        result = resources.stream().filter(s -> StringUtils.isEmpty(s.getParentResourcesNum())).collect(Collectors.toList());
        return result;
    }

    private List<ResourcesDto> findByParentResourcesNum(List<ResourcesDto> list, Integer parentNum){
        List<ResourcesDto> resources = list.stream().filter(s -> parentNum.equals(s.getParentResourcesNum())).collect(Collectors.toList());
        return resources;
    }
}
