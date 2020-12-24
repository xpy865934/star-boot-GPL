package com.star.starboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.config
 * @Description: WebSocketConfig
 * @Author: xpy
 * @Date: Created in 2020年12月24日 7:36 下午
 */
@Configuration
public class WebSocketConfig {
    @Bean

    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
