package com.star.starboot.config.shiro;

import com.star.starboot.constant.SystemConstant;
import com.star.starboot.system.dto.UsersDto;
import com.star.starboot.system.entity.Resources;
import com.star.starboot.system.entity.Roles;
import com.star.starboot.system.service.RolesReResourcesService;
import com.star.starboot.system.service.RolesService;
import com.star.starboot.system.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
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
 * @Description: 用户密码登录Realm
 * @Author: xpy
 * @Date: Created in 2019年06月11日 下午7:26
 */
@Slf4j
public class UserPasswordRealm extends AuthorizingRealm {

    @Autowired
    private UsersService usersService;

    @Autowired
    private RolesService rolesService;

    @Autowired
    private RolesReResourcesService rolesReResourcesService;

    @Autowired
    private RedisSessionDAO redisSessionDAO;

    @Override
    public String getName() {
        return LoginType.USER_PASSWORD.getType();
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        if (token instanceof UserToken) {
            return ((UserToken) token).getLoginType() == LoginType.USER_PASSWORD;
        } else {
            return false;
        }
    }

    @Override
    public void setAuthorizationCacheName(String authorizationCacheName) {
        super.setAuthorizationCacheName(authorizationCacheName);
    }

    @Override
    protected void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    /**
     * 清除所有的缓存权限信息
     */
    public void updateShiroUserAuthorizationInfo(){
        Collection<Session> sessions = redisSessionDAO.getActiveSessions();
        for (Session session:sessions) {
            clearCachedAuthorizationInfo((PrincipalCollection) session.getAttribute("org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY"));
        }
    }

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

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
                        info.addRole(role.getRoleName() + "(" + role.getRoleCode() + ")");

                        // 获取资源列表，所有的菜单和button全部放在一起，有这个资源，前端才展示
                        List<Resources> resourcesList = rolesReResourcesService.getResourcesByRoleTid(role.getRoleId());
                        if(CollectionUtils.isNotEmpty(resourcesList)){
                            for (Resources resource : resourcesList){
                                if(!StringUtils.isEmpty(resource.getResourcesCode())){
                                    info.addStringPermission(resource.getResourcesCode());
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

    /**
     * 认证信息.(身份验证) : Authentication 是用来验证用户身份
     *
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("---------------- 用户密码登录 ----------------------");
        UserToken token = (UserToken) authenticationToken;
        // 用户工号
        String userCode = token.getUsername();
        // 用户选择的公司
        String companyCode = token.getCompanyCode();

        // 从数据库获取对应用户名密码的用户
        UsersDto user = usersService.getUserByUserCodeAndCompanyCode(userCode,companyCode);
        if (user != null) {
            // 用户为禁用状态,1为禁用状态
            if (SystemConstant.USERFORBID.equals(user.getWorking().toString())) {
                throw new DisabledAccountException("账户已被禁用，请联系管理员！");
            }
            SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                    user, //用户
                    user.getPassword(), //密码
                    getName()  //realm name
            );
            return authenticationInfo;
        }
        throw new UnknownAccountException("工号不存在");
    }

    /**
     * 获取用户角色和权限
     * @param principalCollection
     * @return
     */
    public AuthorizationInfo getUserAuthorizationInfo(PrincipalCollection principalCollection){
        return this.doGetAuthorizationInfo(principalCollection);
    }


}
