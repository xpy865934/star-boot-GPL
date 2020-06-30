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
 * 一级字典
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_first_dict")
public class FirstDict extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 一级字典ID
     */
    @TableId("FIRST_DICT_ID")
    private String firstDictId;
    /**
     * 一级字典代码
     */
    @TableField("FIRST_DICT_CODE")
    private String firstDictCode;
    /**
     * 一级字典名称
     */
    @TableField("FIRST_DICT_NAME")
    private String firstDictName;
    /**
     * 一级字典说明
     */
    @TableField("FIRST_DICT_DS")
    private String firstDictDs;
    @TableField("DICT_ID")
    private String dictId;


}
