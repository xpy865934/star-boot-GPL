package com.star.starboot.config.flowable;

import org.flowable.ui.admin.domain.generator.MinimalDataGenerator;
import org.flowable.ui.admin.properties.FlowableAdminAppProperties;
import org.flowable.ui.common.service.idm.RemoteIdmService;
import org.flowable.ui.modeler.properties.FlowableModelerAppProperties;
import org.flowable.ui.modeler.servlet.ApiDispatcherServletConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;


@Configuration
@EnableConfigurationProperties({FlowableModelerAppProperties.class, FlowableAdminAppProperties.class})
@ComponentScan(basePackages = {
//        "org.flowable.ui.idm.conf",
//        "org.flowable.ui.idm.security",
//        "org.flowable.ui.idm.service",
        "org.flowable.ui.admin.repository",
        "org.flowable.ui.admin.service",
        "org.flowable.ui.modeler.repository",
        "org.flowable.ui.modeler.service",
//        "org.flowable.ui.common.filter",
        "org.flowable.ui.common.service",
        "org.flowable.ui.common.repository",
//        "org.flowable.ui.common.security",
        "org.flowable.ui.common.tenant"}, excludeFilters = {
//        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = org.flowable.ui.idm.conf.ApplicationConfiguration.class),
        //移除flowable.cmmon.app 的设置
//        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = EditorUsersResource.class),
//        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = EditorGroupsResource.class),
//        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = RemoteIdmServiceImpl.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = RemoteIdmService.class),
//        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = RemoteIdmAuthenticationProvider.class),
        //移除flowable 中的spring security 的设置
//        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfiguration.class),
//        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfiguration.ApiWebSecurityConfigurationAdapter.class),
//        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfiguration.ActuatorWebSecurityConfigurationAdapter.class),
//        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfiguration.FormLoginWebSecurityConfigurerAdapter.class),
        //编辑器国际化文件 这个在flowable 6.5 版本中前端支持国际化了， 不需要排除了
        //@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = StencilSetResource.class),
//        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = org.flowable.ui.modeler.conf.ApplicationConfiguration.class) ,
        // 排除获取用户信息，采用自定义方式实现
//        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = RemoteAccountResource.class)
})
public class ApplicationConfiguration {


    @Bean
    public ServletRegistrationBean apiServlet(ApplicationContext applicationContext) {
        AnnotationConfigWebApplicationContext dispatcherServletConfiguration = new AnnotationConfigWebApplicationContext();
        dispatcherServletConfiguration.setParent(applicationContext);
        dispatcherServletConfiguration.register(ApiDispatcherServletConfiguration.class);
        DispatcherServlet servlet = new DispatcherServlet(dispatcherServletConfiguration);
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(servlet, "/api/*");
        registrationBean.setName("Flowable IDM App API Servlet");
        registrationBean.setLoadOnStartup(1);
        registrationBean.setAsyncSupported(true);
        return registrationBean;
    }
}
