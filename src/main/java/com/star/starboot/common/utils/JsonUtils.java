package com.star.starboot.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.common.utils
 * @Description: Json工具类
 * @Author: xpy
 * @Date: Created in 2020年07月16日 15:21
 */
@Slf4j
public class JsonUtils {

    public static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * obj 转成json
     *
     * @param obj
     * @return
     */
    public static String obj2Json(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * json 转换成 obj
     *
     * @param json json 字符串
     * @param clz  java class
     * @param <T>  泛型
     * @return
     */
    public static <T> T json2Obj(String json, Class<T> clz) {
        try {
            return objectMapper.readValue(json, clz);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
