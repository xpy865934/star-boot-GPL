package com.star.starboot.person.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.star.starboot.common.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户基本信息
 * </p>
 *
 * @author xpy
 * @since 2020-07-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_user_basic_info")
public class UserBasicInfo extends AbstractEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 用户基本信息id
     */
    @TableId("USER_BASIC_INFO_ID")
    private String userBasicInfoId;
    @TableField("USER_ID")
    private String userId;
    /**
     * 省份
     */
    @TableField("PROVINCE")
    private String province;
    /**
     * 省份代码
     */
    @TableField("PROVINCE_CODE")
    private String provinceCode;
    /**
     * 城市
     */
    @TableField("CITY")
    private String city;
    /**
     * 城市代码
     */
    @TableField("CITY_CODE")
    private String cityCode;
    /**
     * 地区
     */
    @TableField("AREA")
    private String area;
    /**
     * 地区代码
     */
    @TableField("AREA_CODE")
    private String areaCode;
    /**
     * 详细地址
     */
    @TableField("ADDRESS")
    private String address;
    /**
     * 邮编
     */
    @TableField("POSTAL_CODE")
    private String postalCode;
    /**
     * 医院等级
     */
    @TableField("LEVEL")
    private String level;
    /**
     * 总床位数
     */
    @TableField("BED_TOTAL")
    private Integer bedTotal;
    /**
     * ICU床位数
     */
    @TableField("ICU_BED_TOTAL")
    private Integer icuBedTotal;
    /**
     * ICU主任医师
     */
    @TableField("ICU_ZRYS")
    private Integer icuZrys;
    /**
     * ICU副主任医师
     */
    @TableField("ICU_FZRYS")
    private Integer icuFzrys;
    /**
     * ICU主治医师
     */
    @TableField("ICU_ZZYS")
    private Integer icuZzys;
    /**
     * ICU住院医师
     */
    @TableField("ICU_ZYYS")
    private Integer icuZyys;
    /**
     * ICU呼吸治疗师
     */
    @TableField("ICU_HXZLS")
    private Integer icuHxzls;
    /**
     * ICU临床药师
     */
    @TableField("ICU_LCYS")
    private Integer icuLcys;
    /**
     * ICU物理治疗师
     */
    @TableField("ICU_WLZL")
    private Integer icuWlzl;
    /**
     * ICU主任护师
     */
    @TableField("ICU_ZRHS")
    private Integer icuZrhs;
    /**
     * ICU副主任护师
     */
    @TableField("ICU_FZRHS")
    private Integer icuFzrhs;
    /**
     * ICU主管护师人数
     */
    @TableField("ICU_ZGHS")
    private Integer icuZghs;
    /**
     * ICU护师人数
     */
    @TableField("ICU_HSY")
    private Integer icuHsy;
    /**
     * ICU护士人数
     */
    @TableField("ICU_HS")
    private Integer icuHs;
    /**
     * 联系人名称
     */
    @TableField("LXR_NAME")
    private String lxrName;
    /**
     * 联系电话
     */
    @TableField("LXR_TEL")
    private String lxrTel;
    /**
     * 邮箱
     */
    @TableField("LXR_EMAIL")
    private String lxrEmail;

}
