package com.star.starboot.config;

import com.alibaba.druid.util.StringUtils;
import com.star.starboot.config.shiro.*;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.config
 * @Description: Shiro配置类
 * @Author: xpy
 * @Date: Created in 2019年05月29日 下午3:17
 */
@Configuration
public class ShiroConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    /**
     * Shiro 过滤器
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 没有登陆的用户只能访问登陆页面，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
        shiroFilterFactoryBean.setLoginUrl("/common/unauth");
        // 登录成功后要跳转的链接
        //shiroFilterFactoryBean.setSuccessUrl("/auth/index");
        // 未授权界面或者接口
        shiroFilterFactoryBean.setUnauthorizedUrl("/common/unauth");

        //自定义拦截器
        Map<String, Filter> filtersMap = new LinkedHashMap<String, Filter>();
        //限制同一帐号同时在线的个数。
        filtersMap.put("kickout", kickoutSessionControlFilter());
        filtersMap.put("authc", formAuthenticationFilter());

        // TODO 登录过滤器
        shiroFilterFactoryBean.setFilters(filtersMap);

        // 权限控制map.
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // 登录方法
        filterChainDefinitionMap.putAll(AnonAction.anonAction);
//        filterChainDefinitionMap.put("/admin/login*", "anon"); // 表示可以匿名访问
//        filterChainDefinitionMap.put("/sysUser/getList*", "anon"); // 表示可以匿名访问
//        filterChainDefinitionMap.put("/department/getDepartmentsTree*", "anon"); // 表示可以匿名访问

        //此处需要添加一个kickout，上面添加的自定义拦截器才能生效
//        filterChainDefinitionMap.put("/admin/**", "authc,kickout");// 表示需要认证才可以访问

        filterChainDefinitionMap.put("/**", "authc,kickout");// 表示需要认证才可以访问
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * SecurityManager 是 Shiro 架构的核心，通过它来链接Realm和用户(文档中称之为Subject.)
     */
    @Bean
    public SecurityManager securityManager() {
        // 使用多个Realm时，如果用户名密码不正确，会抛出AuthenticationException异常，不会抛出用户名不存在或者
        // 密码错误异常
        // 总结
        // 实现多个Realm必须要保证至少一个能认证通过，否者抛出的AuthenticationException异常。在捕获login(token)时，无法得到具体的错误信息。也就不能准确的知道是用户不存在或则是用户名密码错误。
        // 建议
        // 使用一个Realm验证，通过继承UsernamePasswordToken类拓展，增加额外的变量来实现多种认证方式。
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setAuthenticator(modularRealmAuthenticator());

        List<Realm> realms = new ArrayList<>();
        // 统一角色权限控制realm
        realms.add(authorizingRealm());
        // 用户密码登录realm
        realms.add(userPasswordRealm());
        // 用户手机号验证码登录realm
        realms.add(userPhoneRealm());

        // 授权realm
        securityManager.setRealms(realms);

        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager());
        // 自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    /**
     * 自定义的Realm管理，主要针对多realm
     */
    @Bean("myModularRealmAuthenticator")
    public MyModularRealmAuthenticator modularRealmAuthenticator() {
        MyModularRealmAuthenticator customizedModularRealmAuthenticator = new MyModularRealmAuthenticator();
        // 设置realm判断条件
        customizedModularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());

        return customizedModularRealmAuthenticator;
    }

    @Bean
    public AuthorizingRealm authorizingRealm(){
        AuthorizationRealm authorizationRealm = new AuthorizationRealm();
        authorizationRealm.setName(LoginType.COMMON.getType());

        return authorizationRealm;
    }

    /**
     * 账号密码登录realm
     *
     * @return
     */
    @Bean
    public UserPasswordRealm userPasswordRealm() {
        UserPasswordRealm userPasswordRealm = new UserPasswordRealm();
        userPasswordRealm.setName(LoginType.USER_PASSWORD.getType());
        // 自定义的密码校验器
        userPasswordRealm.setCredentialsMatcher(credentialsMatcher());
        return userPasswordRealm;
    }


    /**
     * 手机号密码登录realm
     *
     * @return
     */
    @Bean
    public UserPhoneRealm userPhoneRealm() {
        UserPhoneRealm userPhoneRealm = new UserPhoneRealm();
        userPhoneRealm.setName(LoginType.USER_PHONE.getType());
        // 自定义的密码校验器
        userPhoneRealm.setCredentialsMatcher(credentialsMatcher());
        return userPhoneRealm;
    }

    @Bean
    public CredentialsMatcher credentialsMatcher() {
        return new CredentialsMatcher();
    }

    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        //设置前缀
        redisCacheManager.setKeyPrefix("QCNT_CACHE:");
        // redis针对不同公司，不同用户缓存
        redisCacheManager.setPrincipalIdFieldName("userCompanyCode");
        return redisCacheManager;
    }

    /**
     * cacheManager App缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    public RedisCacheManager appCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        //设置前缀
        redisCacheManager.setKeyPrefix("QCNT_APP_CACHE:");
        // redis针对不同公司，不同用户缓存
        redisCacheManager.setPrincipalIdFieldName("userCompanyCode");
        return redisCacheManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        redisSessionDAO.setKeyPrefix("QCNT_SESSION:");
        // session在redis中的保存时间,最好大于session会话超时时间
        redisSessionDAO.setExpire(43200);
        return redisSessionDAO;
    }

    /**
     * Session Manager
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public SessionManager sessionManager() {
        SimpleCookie simpleCookie = new SimpleCookie("Token");
        simpleCookie.setPath("/");
        simpleCookie.setHttpOnly(false);

        MySessionManager sessionManager = new MySessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        sessionManager.setSessionIdCookieEnabled(true);// 需要设置为true，否则druid登录无法获取session
        sessionManager.setSessionIdCookie(simpleCookie);

        //全局会话超时时间（单位毫秒），默认30分钟  暂时设置为10秒钟 用来测试
        sessionManager.setGlobalSessionTimeout(43200000);  // 设置为12小时
        //是否开启删除无效的session对象  默认为true
        sessionManager.setDeleteInvalidSessions(true);
        //是否开启定时调度器进行检测过期session 默认为true
        sessionManager.setSessionValidationSchedulerEnabled(true);
        //设置session失效的扫描时间, 清理用户直接关闭浏览器造成的孤立会话 默认为 1个小时
        //设置该属性 就不需要设置 ExecutorServiceSessionValidationScheduler 底层也是默认自动调用ExecutorServiceSessionValidationScheduler
        //暂时设置为 5秒 用来测试
        sessionManager.setSessionValidationInterval(3600000);
        //取消url 后面的 JSESSIONID
        sessionManager.setSessionIdUrlRewritingEnabled(false);

        return sessionManager;
    }


    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisHost + ":" + redisPort);
//        redisManager.setPort(redisPort);
        redisManager.setTimeout(43200); //设置过期时间
        // 如果密码为空，不需要设置，设置了会报错ERR Client sent AUTH, but no password is set
        if(!StringUtils.isEmpty(redisPassword)){
            redisManager.setPassword(redisPassword);
        }
        return redisManager;
    }

    /**
     * 限制同一账号登录同时登录人数控制
     *
     * @return
     */
    @Bean
    public MySessionControlFilter kickoutSessionControlFilter() {
        MySessionControlFilter kickoutSessionControlFilter = new MySessionControlFilter();
        kickoutSessionControlFilter.setCache(cacheManager());
        kickoutSessionControlFilter.setAppCache(appCacheManager());
        kickoutSessionControlFilter.setSessionManager(sessionManager());
        kickoutSessionControlFilter.setKickoutAfter(false);
        kickoutSessionControlFilter.setMaxSession(1);
        kickoutSessionControlFilter.setKickoutUrl("/common/kickout");
        return kickoutSessionControlFilter;
    }

    /**
     * ajax options请求过滤和授权失败302过滤
     * @return
     */
    public ShiroFormAuthenticationFilter formAuthenticationFilter(){
        return new ShiroFormAuthenticationFilter();
    }



    /***
     * 授权所用配置
     *
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /***
     * 使授权注解起作用不如不想配置可以在pom文件中加入
     * <dependency>
     *<groupId>org.springframework.boot</groupId>
     *<artifactId>spring-boot-starter-aop</artifactId>
     *</dependency>
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * Shiro生命周期处理器
     * 此方法需要用static作为修饰词，否则无法通过@Value()注解的方式获取配置文件的值
     *
     */
    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
}
