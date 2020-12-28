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
     * 方案设计
     */
    public static final String DESIGN  = "DESIGN";
    /**
     * 项目流程
     */
    public static final String PROJECT = "PROJECT";
    /**
     * 售后流程
     */
    public static final String AFTERSALES = "AFTERSALES";

    /**
     * 售后流程节点
     */
    public enum  AFTERSALESNODES {
        /**
         * 维修
         */
        wx("wx"),

        /**
         * 分配安装师傅
         */
        fpazsf("fpazsf"),

        /**
         * 审核
         */
        sh("sh");
        /**
         * 节点名称
         */
        private String name;

        AFTERSALESNODES(String name) {
            this.name = name;
        }

    }

    /**
     * 流程状态  1  申请  2  审批中  3 结束
     */
    public static final Integer PROCESS_START = 1;
    public static final Integer PROCESS_APPROVING = 2;
    public static final Integer PROCESS_COMPLETE = 3;

    /**
     * 配置表及方案设计类型
     */
    public static final String CUSTOMER_PLAN = "customerPlan";
    public static final String CONFIG_TABLE = "configTable";
    public static final String PLAN_DRAW = "planDraw";

    /**
     * 项目状态
     */
    public enum  ProjectStatus {
        /**
         * 申请
         */
        CREATE("1", "申请"),
        /**
         * 设计
         */
        DESIGN("2", "设计");

        /**
         * 项目状态代码
         */
        private String status;
        /**
         * 项目状态名
         */
        private String name;


        public String getStatus() {
            return status;
        }

        ProjectStatus(String status, String name) {
            this.status = status;
            this.name = name;
        }

    }

    /**
     * 提成类型
     */
    public enum  CommissionType {
        /**
         * 售后提成
         */
        AFTERSALES("01", "售后提成");

        /**
         * 提成类型代码
         */
        private String status;
        /**
         * 提成类型名称
         */
        private String name;


        public String getStatus() {
            return status;
        }

        CommissionType(String status, String name) {
            this.status = status;
            this.name = name;
        }

    }

    /**
     * 已结清
     */
    public static final String SETTLE_CLEAR = "02";

    /**
     * 未结清
     */
    public static final String NO_SETTLE_CLEAR = "01";

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
    public static final String WORKING = "01";


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

    /**
     * 流程提交通过
     */
    public static final String APPROVAL = "提交";

    /**
     * 流程提交退回
     */
    public static final String BACK = "退回";
}
