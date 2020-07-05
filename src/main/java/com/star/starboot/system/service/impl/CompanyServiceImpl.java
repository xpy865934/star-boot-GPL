package com.star.starboot.system.service.impl;

import com.star.starboot.system.entity.Company;
import com.star.starboot.system.dao.CompanyMapper;
import com.star.starboot.system.service.CompanyService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 公司信息 服务实现类
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {

}
