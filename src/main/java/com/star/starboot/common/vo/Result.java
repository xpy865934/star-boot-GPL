package com.star.starboot.common.vo;

import com.star.starboot.common.enums.ResultCode;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.common.dto
 * @Description: 请求返回结果类
 * @Author: xpy
 * @Date: Created in 2020年07月01日 14:04
 */
public class Result {
    int code;
    String msg;
    Object data;

    public static Result create(int code, String msg) {
        return new Result().setCode(code).setMsg(msg);
    }

    public static Result create(int code, String msg, Object data) {
        return create(code, msg).setData(data);
    }
    public static Result create(ResultCode resultCode, Object data) {
        return create(resultCode.getCode(), resultCode.getMsg()).setData(data);
    }
    public static Result create(ResultCode resultCode) {
        return create(resultCode.getCode(), resultCode.getMsg(),null);
    }

    public static Result success() {
        return Result.create(ResultCode.OK,null);
    }

    public static Result success(String msg) {
        return Result.create(ResultCode.OK.getCode(),msg,null);
    }

    public static Result success(Object data) {
        return success().setData(data);
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public Object getData() {
        return this.data;
    }

    public Result setCode(int code) {
        this.code = code;
        return this;
    }

    public Result setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Result setData(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Result)) {
            return false;
        } else {
            Result other = (Result)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getCode() != other.getCode()) {
                return false;
            } else {
                Object this$msg = this.getMsg();
                Object other$msg = other.getMsg();
                if (this$msg == null) {
                    if (other$msg != null) {
                        return false;
                    }
                } else if (!this$msg.equals(other$msg)) {
                    return false;
                }

                Object this$data = this.getData();
                Object other$data = other.getData();
                if (this$data == null) {
                    if (other$data != null) {
                        return false;
                    }
                } else if (!this$data.equals(other$data)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof Result;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 59 + this.getCode();
        Object $msg = this.getMsg();
        result = result * 59 + ($msg == null ? 43 : $msg.hashCode());
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Result(code=" + this.getCode() + ", msg=" + this.getMsg() + ", data=" + this.getData() + ")";
    }
}
