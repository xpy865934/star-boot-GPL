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
 * 系统日志
 * </p>
 *
 * @author xpy
 * @since 2020-07-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_sys_log")
public class SysLog extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @TableId("LOG_ID")
    private String logId;
    /**
     * 请求方法
     */
    @TableField("REQUIRE_METHOD")
    private String requireMethod;
    /**
     * 方法描述
     */
    @TableField("METHOD_DESC")
    private String methodDesc;
    /**
     * IP
     */
    @TableField("IP")
    private String ip;
    /**
     * 客户端
     */
    @TableField("CLIENT")
    private String client;
    /**
     * 操作系统
     */
    @TableField("OS")
    private String os;
    /**
     * 请求参数
     */
    @TableField("PARAMS")
    private String params;
    @TableField("USER_ID")
    private String userId;
    @TableField("COMPANY_ID")
    private String companyId;


}
