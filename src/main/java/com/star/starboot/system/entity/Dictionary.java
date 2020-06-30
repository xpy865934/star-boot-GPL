package com.star.starboot.system.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.star.starboot.common.entity.AbstractEntity;

import com.baomidou.mybatisplus.annotations.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统字典表
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_dictionary")
public class Dictionary extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 数据字典ID
     */
    @TableId("DICT_ID")
    private String dictId;
    /**
     * 数据字典名称
     */
    @TableField("DICT_NAME")
    private String dictName;
    /**
     * 数据字典描述
     */
    @TableField("DICT_DS")
    private String dictDs;
    /**
     * 数据字典类型 1 系统字典  2 用户字典
     */
    @TableField("DICT_TY")
    private Integer dictTy;
    /**
     * 数据字典编号
     */
    @TableField("DICT_NUM")
    private Integer dictNum;


}
