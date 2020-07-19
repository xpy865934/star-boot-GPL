package com.star.starboot.person.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.starboot.person.dao.UserBasicInfoMapper;
import com.star.starboot.person.entity.UserBasicInfo;
import com.star.starboot.person.service.UserBasicInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户基本信息 服务实现类
 * </p>
 *
 * @author xpy
 * @since 2020-07-19
 */
@Service
public class UserBasicInfoServiceImpl extends ServiceImpl<UserBasicInfoMapper, UserBasicInfo> implements UserBasicInfoService {

}
