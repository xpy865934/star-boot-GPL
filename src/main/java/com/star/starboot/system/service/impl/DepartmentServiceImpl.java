package com.star.starboot.system.service.impl;

import com.star.starboot.system.entity.Department;
import com.star.starboot.system.dao.DepartmentMapper;
import com.star.starboot.system.service.DepartmentService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 组织架构 服务实现类
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

}
