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
        // ureport2
        put("/ureport/**", "anon");

        // 公共请求
        put("/sysUser/getAllUserInfo*", "anon");
        put("/sysProcessDetail/getNextTask*", "anon");
        put("/sysProcessDetail/taskSubmit*", "anon");
        put("/sysProcessDetail/taskBack*", "anon");
        put("/contract/getAllContract","anon");
        put("/sysAppSetting/getAppVersion","anon");
        put("/sysAppSetting/downloadAppUpdate","anon");
        put("/resources/queryList","anon");

        // 部门、组织机构
        put("/department/getDepartmentsTree*", "anon");
        put("/department/update*", "anon");
        put("/department/delete*", "anon");
        put("/department/save*", "anon");
        // 公司
        put("/company/getList*", "anon");
        // 用户
        put("/sysUser/getUserInfo*", "anon");
        put("/sysUser/getList*", "anon");
        put("/sysUser/save*", "anon");
        put("/sysUser/update*", "anon");
        // 判断需要：IOS
        put("/sysUser/register*", "anon");
        // 角色
        put("/sysRoles/save*", "anon");
        put("/sysRoles/update*", "anon");
        put("/sysRoles/getList*", "anon");
        put("/sysRoles/getListByCompantId*", "anon");
        put("/userRolesRelation/save*", "anon");
        put("/userRolesRelation/getList*", "anon");
        // 资源
        put("/sysResources/getResourcesTree*","anon");
        put("/sysResources/getResourcesByRoleTid*","anon");
        put("/roleResourcesRelation/save*","anon");
        // 文件
        put("/file/upload*", "anon");

        // 字典
        put("/sysDictDetail/getDict*","anon");
        put("/sysDict/getDictBytype*","anon");

        // 客户
        put("/customerProposal/downloadDesign*","anon");
        put("/customerInfo/getCustomerInfoList*","anon");

        // 仓库
        put("/wareHouse/checkWareHouseNum", "anon");

        // 合同
        put("/contract/getContractCustomerList", "anon");

        // 付款比例
        put("/paymentRatio/getPaymentRatioByType", "anon");

        // ios审核
        put("/iosNews/getList", "anon");
        put("/iosProduct/getList", "anon");

        // 临时测试
        put("/resources/queryList", "anon");
        put("/resources/updateUsed", "anon");
        put("/resources/deleteById", "anon");
        put("/resources/save", "anon");
        put("/resources/update", "anon");
    }};
}
