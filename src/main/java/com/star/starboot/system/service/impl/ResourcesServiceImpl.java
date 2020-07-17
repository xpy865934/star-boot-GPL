package com.star.starboot.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.starboot.system.entity.Resources;
import com.star.starboot.system.dao.ResourcesMapper;
import com.star.starboot.system.service.ResourcesService;
import org.springframework.stereotype.Service;

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

}
