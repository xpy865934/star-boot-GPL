package com.star.starboot.system.entity;

import java.util.Date;
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
 * 用户信息表
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_users")
public class Users extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId("USER_ID")
    private String userId;
    /**
     * 用户工号
     */
    @TableField("USER_CODE")
    private String userCode;
    /**
     * 用户姓名
     */
    @TableField("USRE_NAME")
    private String usreName;
    /**
     * 用户性别
     */
    @TableField("USRE_SEX")
    private String usreSex;
    @TableField("DEPARTMENT_ID")
    private String departmentId;
    /**
     * 公司职务
     */
    @TableField("COMPANY_POSITION")
    private String companyPosition;
    /**
     * 出生日期
     */
    @TableField("BIRTHDAY")
    private Date birthday;
    /**
     * 证件类型
     */
    @TableField("CREDENTIALS")
    private String credentials;
    /**
     * 证件号码
     */
    @TableField("CREDENTIALS_CODE")
    private String credentialsCode;
    /**
     * 电子邮箱
     */
    @TableField("EMAIL")
    private String email;
    /**
     * QQ
     */
    @TableField("QQ")
    private String qq;
    /**
     * 微信
     */
    @TableField("WX")
    private String wx;
    /**
     * 手机号码
     */
    @TableField("TEL")
    private String tel;
    /**
     * 登录密码
     */
    @TableField("PASSWORD")
    private String password;
    /**
     * 密码修改时间
     */
    @TableField("PASSWORD_CHANGE")
    private Date passwordChange;
    /**
     * 在职情况  1 离职 0 在职
     */
    @TableField("WORKING")
    private Integer working;
    /**
     * 盐
     */
    @TableField("SALT")
    private String salt;


}
