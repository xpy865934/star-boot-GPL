package com.star.starboot.common.enums;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.qcnt.qcnt.system.enums
 * @Description: 返回值code
 * @Author: xpy
 * @Date: Created in 2019年06月11日 上午10:06
 */
public enum ResultCode {
    /**
     * OK
     */
    OK(200, ""),
    USER_INFO_LOSE_ERROR(10005, "登录信息失效，请重新登录"),
    NOT_EXIST_USER_OR_ERROR_PWD(10000, "该用户不存在或密码错误"),
    SUCCESS_SAVE(200, "保存成功！"),
    SUCCESS_UPLOAD(200, "上报成功！"),
    ERROR_PARAMS(110, "参数异常！"),
    SIGN_ERROR(120, "签名错误"),
    TIME_OUT(130, "访问超时"),
    BAD_REQUEST(400, "参数解析失败"),
    INVALID_TOKEN(401, "无效的授权码"),
    INVALID_CLIENTID(402, "无效的密钥"),
    METHOD_NOT_ALLOWED(405, "不支持当前请求方法"),
    SYSTEM_ERR(500, "服务器运行异常"),
    LOGINED_IN(10001, "该用户已登录"),
    NOT_EXIST_BUSINESS(10002, "该商家不存在"),
    SHIRO_ERROR(10003, "登录异常"),
    UNAUTHO_ERROR(10004, "无权限访问"),
    BIND_PHONE(10010, "请绑定手机号"),
    UPLOAD_ERROR(20000, "上传文件异常"),
    INVALID_CAPTCHA(30005, "无效的验证码"),
    USER_FROZEN(40000, "该用户已被冻结"),
    USER_DISABLED(40001, "账户已被禁用，请联系管理员！"),
    ERROR_QUERY_FAILED(40002, "查询失败！"),
    ERROR_SAVE_FAILED(40003, "保存失败！"),
    ERROR_UPDATE_FAILED(40004, "修改失败！"),
    ERROR_DELETED_FAILED(40005, "删除失败！"),
    ERROR_SUBMIT_FAILED(40006, "提交失败！"),
    ERROR_UPLOAD_FAILED(40007, "上传失败！"),
    ERROR_BACK_FAILED(40008, "退回失败！"),
    ERROR_PASSWORD_CHANGE(40009, "旧密码不正确！");

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
