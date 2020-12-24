package com.star.starboot.config.shiro;

import java.util.HashMap;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.qcnt.qcnt.constant
 * @Description: 系统可以匿名访问的接口
 * @Author: xpy
 * @Date: Created in 2019年08月07日 01:55
 */
public class AnonAction {
    public static final Map<String,String> anonAction = new HashMap<String,String>(){{
        // druid
        put("/druid/**", "anon");
        // swagger
        put("/swagger/**", "anon");
        // ureport2
//        put("/ureport/**", "anon");

        // 公共请求(登录方法放在了公共请求里面)
        put("/common/**", "anon");
        put("/file/**", "anon");
        put("/users/getUserInfo*", "anon");
        put("/users/queryList*", "anon");
        put("/firstDict/getFirstDictAll*", "anon");
        put("/customerHouses/getHousesByCustomerInfoId*", "anon");
        put("/customerHouses/queryCustomerHousesInfo*", "anon");
        put("/customerInfo/checkTel*", "anon");
        put("/customerHousesProject/queryVersion*", "anon");
        put("/customerHousesProject/queryPlanAndConfigTable*", "anon");
        put("/afterSales/queryById*", "anon");
        put("/equipment/queryById*", "anon");
        put("/appConfig/getAppVersion*", "anon");
        put("/appConfig/downloadAppUpdate*", "anon");
        put("/commission/queryTotalByUserId*", "anon");
        put("/company/getList*", "anon");
        put("/department/getListByCompanyId*", "anon");
        put("/users/queryById*", "anon");
        put("/roles/getListByCompantId*", "anon");
        put("/usersReRoles/getRolesByUserId*", "anon");
        put("/websocket/*", "anon");
        put("/commission/queryUserCommissionPager*", "anon");

        // 下面shiro不拦截，交给FLowableLoginAdapter去拦截进行操作
        put("/app/**", "anon");
        put("/process-api/**", "anon");
        put("/app-api/**", "anon");
        put("/starflowable/**", "anon");
        put("/flowableAdminLogin", "anon");
        put("/flowableAdminLogout", "anon");
        // 静态资源
        put("/static/**", "anon");
        put("/admin/**", "anon");

        // 判断需要：IOS
        put("/sysUser/register*", "anon");

        // ios审核
        put("/iosnews/getList", "anon");
        put("/iosProduct/getList", "anon");
    }};
}
