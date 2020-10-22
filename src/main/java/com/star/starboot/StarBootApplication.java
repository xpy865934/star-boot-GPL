package com.star.starboot;

import com.star.starboot.config.flowable.AppDispatcherServletConfiguration;
import com.star.starboot.config.flowable.ApplicationConfiguration;
import com.star.starboot.config.flowable.DatabaseAutoConfiguration;
import org.flowable.ui.admin.domain.generator.MinimalDataGenerator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ServletComponentScan
@SpringBootApplication(scanBasePackages = {"com.star.starboot"},exclude = {SecurityAutoConfiguration.class})
@EnableCaching
@EnableAsync
@MapperScan("com.star.starboot.*.dao*")
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
//@ImportResource("classpath:ureport-console-context.xml")
@EnableTransactionManagement
@Import({
        ApplicationConfiguration.class,
        AppDispatcherServletConfiguration.class,
        DatabaseAutoConfiguration.class,
        MinimalDataGenerator.class
//        DatabaseConfiguration.class
})
public class StarBootApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(StarBootApplication.class, args);
    }

    @Override//为了打包springboot项目
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }
}
