package com.star.starboot.constant;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.constant
 * @Description: 系统常量
 * @Author: xpy
 * @Date: Created in 2019年06月11日 下午7:48
 */
public class SystemConstant {
    // 流程图
    public static final String KHGL  = "KHGL";

    /**
     * 流程状态  1  申请  2  审批中  3 结束
     */
    public static final Integer PROCESS_START = 1;
    public static final Integer PROCESS_APPROVING = 2;
    public static final Integer PROCESS_COMPLETE = 3;


    /**
     * 年月日格式化
     */
    public static final String FULL_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static final String DATE_GAPLESS_PATTERN = "yyyyMMdd";

    public static final String DATE_YEAR_PATTERN = "yyyy";

    public static final String DATE_MONTH_PATTERN = "yyyy-MM";


    /**
     * 通用是否标记
     */
    public static final Integer COMMON_FLAG_TRUE = 1;
    public static final Integer COMMON_FLAG_FALSE = 0;

    /**
     * 空串
     */
    public static final String NULL_STR = "";

    /**
     * 用户禁用
     */
    public static final String USERFORBID = "1";

    /**
     * 在职
     */
    public static final Integer WORKING =0;


    /**
     * 数据删除标记
     */
    public static final Integer DATADELETED = 1;

    /**
     * 普通消息
     */
    public static final Integer NORMALMSG = 1;

    /**
     * 流程消息
     */
    public static final Integer FLOWABLEMSG = 2;

    /**
     * APP消息
     */
    public static final String MSG_APP = "MSG_APP";

    /**
     * 系统消息
     */
    public static final String MSG_SYSTEM = "MSG_SYSTEM";

    /**
     * 邮件消息
     */
    public static final String MSG_EMAIL = "MSG_EMAIL";

    /**
     * 注册用户  判断需要：IOS
     */
    public static final Integer REGISTER = 1;
}
