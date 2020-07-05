package com.star.starboot.system.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.star.starboot.common.entity.AbstractEntity;

import com.baomidou.mybatisplus.annotations.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 二级字典
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_second_dict")
public class SecondDict extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 二级字典ID
     */
    @TableField("SECOND_DICT_ID")
    private String secondDictId;
    @TableField("DICT_ID")
    private String dictId;
    @TableField("FIRST_DICT_ID")
    private String firstDictId;
    /**
     * 二级代码代码
     */
    @TableField("SECOND_CODE")
    private String secondCode;
    /**
     * 二级代码名称
     */
    @TableField("SECOND_NAME")
    private String secondName;
    /**
     * 二级代码说明
     */
    @TableField("SECOND_DS")
    private String secondDs;


}
