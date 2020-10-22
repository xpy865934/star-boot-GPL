package com.star.starboot.config.flowable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class FlowableUserLoginAdapter implements WebMvcConfigurer {
    @Autowired
    private FLowableUserLoginInterceptor fLowableUserLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加对用户是否登录的拦截器，并添加过滤项、排除项
        registry.addInterceptor(fLowableUserLoginInterceptor).addPathPatterns("/static/**").addPathPatterns("/app/**").addPathPatterns("/starflowable/**")
                .excludePathPatterns("/static/display/**",
                        "/static/display-cmmn/**",
                        "/static/editor-app/**",
                        "/static/fonts/**",
                        "/static/i18n/**",
                        "/static/images/**",
                        "/static/libs/**",
                        "/static/login/**",
                        "/static/scripts/**",
                        "/static/styles/**",
                        "/static/libs/**",
                        "/static/404.xml",
                        "/static/browserconfig.xml",
                        "/static/favicon.ico",
                        "/static/manifest.json")
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/additional_components/**",
                        "/admin/bower_components/**",
                        "/admin/display/**",
                        "/admin/display-cmmn/**",
                        "/admin/error/**",
                        "/admin/fonts/**",
                        "/admin/i18n/**",
                        "/admin/images/**",
                        "/admin/scripts/**",
                        "/admin/styles/**",
                        "/admin/browserconfig.xml",
                        "/admin/favicon.ico",
                        "/admin/manifest.json")
                .addPathPatterns("/task/**")
                .excludePathPatterns("/task/display/**",
                        "/task/display-cmmn/**",
                        "/task/error/**",
                        "/task/fonts/**",
                        "/task/i18n/**",
                        "/task/images/**",
                        "/task/libs/**",
                        "/task/scripts/**",
                        "/task/styles/**",
                        "/task/workflow/**",
                        "/task/browserconfig.xml",
                        "/task/favicon.ico",
                        "/task/manifest.json");//排除样式、脚本、图片等资源文件、登录页面

    }
}
