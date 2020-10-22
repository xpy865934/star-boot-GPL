package com.star.starboot.config.flowable;

import org.flowable.ui.admin.properties.FlowableAdminAppProperties;
import org.flowable.ui.modeler.properties.FlowableModelerAppProperties;
import org.flowable.ui.modeler.rest.app.EditorGroupsResource;
import org.flowable.ui.modeler.rest.app.EditorUsersResource;
import org.flowable.ui.modeler.rest.app.StencilSetResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@EnableConfigurationProperties({FlowableModelerAppProperties.class, FlowableAdminAppProperties.class})
@ComponentScan(value = {
//        "org.flowable.ui.idm.rest.app",
//        "org.flowable.ui.common.rest.exception",
//        "org.flowable.ui.common.rest",
        "org.flowable.ui.admin.rest",
        "org.flowable.ui.modeler.rest.app"
},
        excludeFilters = {
//                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = RemoteAccountResource.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = StencilSetResource.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = EditorUsersResource.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = EditorGroupsResource.class)
        })
@EnableAsync
public class AppDispatcherServletConfiguration implements WebMvcRegistrations {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppDispatcherServletConfiguration.class);

    @Bean
    public SessionLocaleResolver localeResolver() {
        return new SessionLocaleResolver();
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LOGGER.debug("Configuring localeChangeInterceptor");
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language");
        return localeChangeInterceptor;
    }

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        LOGGER.debug("Creating requestMappingHandlerMapping");
        RequestMappingHandlerMapping requestMappingHandlerMapping = new RequestMappingHandlerMapping();
        requestMappingHandlerMapping.setUseSuffixPatternMatch(false);
        requestMappingHandlerMapping.setRemoveSemicolonContent(false);
        Object[] interceptors = {localeChangeInterceptor()};
        requestMappingHandlerMapping.setInterceptors(interceptors);
        return requestMappingHandlerMapping;
    }

    /**
     * 重写Liquibase生成表方法，解决找不到ACT_DE_MODE报错
     * @param dataSource
     * @return
     */
//    @Bean
//    public Liquibase liquibase(DataSource dataSource) {
//        LOGGER.info("Liquibase 开始生成模板信息表");
//        Liquibase liquibase = null;
//        try {
//            DatabaseConnection connection = new JdbcConnection(dataSource.getConnection());
//            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(connection);
//            database.setDatabaseChangeLogTableName("ACT_DE_" + database.getDatabaseChangeLogTableName());
//            database.setDatabaseChangeLogLockTableName("ACT_DE_" + database.getDatabaseChangeLogLockTableName());
//
//            liquibase = new Liquibase("META-INF/liquibase/flowable-modeler-app-db-changelog.xml", new ClassLoaderResourceAccessor(), database);
//            liquibase.update("flowable");
//            return liquibase;
//
//        } catch (Exception e) {
//            throw new InternalServerErrorException("生成模板表失败:", e);
//        } finally {
//            if (liquibase != null) {
//                Database database = liquibase.getDatabase();
//                if (database != null) {
//                    try {
//                        database.close();
//                    } catch (DatabaseException e) {
//                        LOGGER.warn("关闭Liquibase生成模板表失败:", e);
//                    }
//                }
//            }
//        }
//    }


}
