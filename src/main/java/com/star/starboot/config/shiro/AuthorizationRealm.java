package com.star.starboot.config.shiro;

import com.star.starboot.system.dto.UsersDto;
import com.star.starboot.system.entity.Resources;
import com.star.starboot.system.entity.Roles;
import com.star.starboot.system.service.RolesReResourcesService;
import com.star.starboot.system.service.RolesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.config.shiro
 * @Description: 统一角色授权控制
 * @Author: xpy
 * @Date: Created in 2020年12月03日 9:40 上午
 */
@Slf4j
public class AuthorizationRealm extends AuthorizingRealm {


    @Autowired
    private RolesService rolesService;

    @Autowired
    private RolesReResourcesService rolesReResourcesService;

    @Autowired
    private RedisSessionDAO redisSessionDAO;

    /**
     * 清除所有的缓存权限信息
     */
    public void updateShiroUserAuthorizationInfo() {
        Collection<Session> sessions = redisSessionDAO.getActiveSessions();
        for (Session session : sessions) {
            clearCachedAuthorizationInfo((PrincipalCollection) session.getAttribute("org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY"));
        }
    }

    @Override
    protected SimpleAuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("---------------- 执行 Shiro 权限获取 ---------------------");
        Object principal = principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (principal instanceof UsersDto) {
            UsersDto userLogin = (UsersDto) principal;
            if(userLogin != null){
                // 查询用户在该公司下面所有的角色
                List<Roles> roleList = rolesService.findByUserIdAndCompany(userLogin.getUserId(),userLogin.getCompanyId());
                if(CollectionUtils.isNotEmpty(roleList)){
                    for(Roles role : roleList){
                        info.addRole(role.getRoleId());

                        // 获取资源列表，所有的菜单和button全部放在一起，有这个资源，前端才展示
                        List<Resources> resourcesList = rolesReResourcesService.getResourcesByRoleTid(role.getRoleId());
                        if(CollectionUtils.isNotEmpty(resourcesList)){
                            for (Resources resource : resourcesList){
                                if(!StringUtils.isEmpty(resource.getResourcesCode())){
                                    // 使用分号分隔一个按钮可能会出现多个接口权限问题
                                    String resourcesCode = resource.getResourcesCode();
                                    String[] resourcesCodes = new String[1024];
                                    if(!StringUtils.isEmpty(resourcesCode)){
                                        resourcesCodes = resourcesCode.split(";");
                                    }
                                    for (int i = 0; i < resourcesCodes.length; i++) {
                                        info.addStringPermission(resourcesCodes[i]);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // 将权限信息存储起来
        SecurityUtils.getSubject().getSession().setAttribute("permissions", info);

        log.info("---------------- 获取到以下权限 ----------------");
        log.info(info.getStringPermissions() == null ? "[]":info.getStringPermissions().toString());
        log.info("---------------- Shiro 权限获取成功 ----------------------");
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        return null;
    }

    /**
     * 获取用户角色和权限
     * @param principalCollection
     * @return
     */
    public SimpleAuthorizationInfo getUserAuthorizationInfo(PrincipalCollection principalCollection){
        return this.doGetAuthorizationInfo(principalCollection);
    }
}
