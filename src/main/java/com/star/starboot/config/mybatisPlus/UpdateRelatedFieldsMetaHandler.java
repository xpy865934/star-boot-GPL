package com.star.starboot.config.mybatisPlus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.star.starboot.common.utils.ShiroUtils;
import com.star.starboot.system.dto.UsersDto;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 * @Package com.star.starboot.config.`mybatis-plus`
 * @Description:  mybatis自动填充相关字段
 * @Author: xpy
 * @Date:   Created in 2020年09月19日 4:43 下午
 * @version V1.0
 */
@Component
public class UpdateRelatedFieldsMetaHandler implements MetaObjectHandler {

    /**
     * 新增操作
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        UsersDto userInfo = ShiroUtils.build().getUserInfo();
        if(!StringUtils.isEmpty(userInfo)) {
            this.setInsertFieldValByName("createBy", userInfo.getUserId(), metaObject);
        }
        this.setInsertFieldValByName("createAt", new Date(), metaObject);
    }

    /**
     * 更新操作
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        UsersDto userInfo = ShiroUtils.build().getUserInfo();
        if(!StringUtils.isEmpty(userInfo)) {
            this.setUpdateFieldValByName("updateBy", userInfo.getUserId(), metaObject);
        }
        this.setUpdateFieldValByName("updateAt", new Date(), metaObject);
    }
}