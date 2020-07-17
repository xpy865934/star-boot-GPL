package com.star.starboot.exception;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.exception
 * @Description: 业务数据异常处理
 * @Author: xpy
 * @Date: Created in 2020年07月16日 15:09
 */
public class BusinessException extends RuntimeException{
    public BusinessException(String msg) {
        super(msg);
    }
}
