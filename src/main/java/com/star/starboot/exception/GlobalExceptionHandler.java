package com.star.starboot.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.star.starboot.common.enums.ResultCode;
import com.star.starboot.common.utils.JsonUtils;
import com.star.starboot.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.exception
 * @Description: 全局异常处理
 * @Author: xpy
 * @Date: Created in 2020年07月16日 15:14
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private HttpServletRequest request;

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handlerException(Exception e) {
        log.error("操作失败 ------------->");
        log.error("url：" + request.getRequestURI());
        log.error("参数：" + JsonUtils.obj2Json(request.getParameterMap()));
        log.error("异常：" + e.getClass().getSimpleName());
        log.error("原因：" + e.getMessage());
        if (!(e instanceof BusinessException)) {
            log.error("详细信息：", e);
        }
        log.error("操作失败 <-------------\n");
        String msg = getMsg(e);
        return Result.create(ResultCode.SYSTEM_ERR.getCode(),StringUtils.isEmpty(msg) ? ResultCode.SYSTEM_ERR.getMsg() : msg);
    }

    public static String getMsg(Throwable e) {
        if (e instanceof BusinessException) {
            return e.getMessage();
        } else {
            // 其他异常 直接返回空
            return null;
        }
    }

}
