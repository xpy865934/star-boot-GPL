package com.star.starboot.aop;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.aop
 * @Description: 数字类型小数位数格式化
 * @Author: xpy
 * @Date: Created in 2021年02月24日 2:34 下午
 */
public class DoubleJsonSerializer extends JsonSerializer<Double> {

    @Value("${starboot.decimal}")
    private int decimal;

    private DecimalFormat df = new DecimalFormat("0.00");

    /**
     * 保留小数位数序列化器
     *
     * @param data
     * @param jsonGenerator
     * @param serializerProvider
     * @throws IOException
     */
    @Override
    public void serialize(Double data, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (data != null) {
            String pattern = "0.";
            for (int i = 0; i < decimal; i++) {
                pattern += "0";
            }
            df.applyPattern(pattern);
            if (data == 0) {
                jsonGenerator.writeString("0");
            } else {
                jsonGenerator.writeString(df.format(data));
            }
        }
    }
}
