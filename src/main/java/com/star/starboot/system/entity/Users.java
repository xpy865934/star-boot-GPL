package com.star.starboot.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.star.starboot.common.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

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
    @TableField("USER_NAME")
    private String userName;
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
    /**
     * 最后一次的登录时间
     */
    @TableField("LAST_LOGIN_DATE")
    private Date lastLoginDate;
    /**
     * 最后一次登录的ip地址
     */
    @TableField("LAST_LOGIN_IP")
    private String lastLoginIp;
    /**
     * Uni push 客户端ID
     */
    @TableField("CLIENT_ID")
    private String clientId;
    /**
     * uni push appID
     */
    @TableField("APP_ID")
    private String appId;
    /**
     * uni push appkey
     */
    @TableField("APP_KEY")
    private String appKey;
    /**
     * 是否是注册用户 1 注册用户 0 非注册用户
     */
    @TableField("REGISTER")
    private Integer register;
}
