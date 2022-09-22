package com.star.starboot.system.controller;


import ch.qos.logback.core.util.ContextUtil;
import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.starboot.annotation.SysLog;
import com.star.starboot.common.controller.AbstractController;
import com.star.starboot.common.utils.ContextUtils;
import com.star.starboot.common.vo.Result;
import com.star.starboot.system.dto.UsersReRolesDto;
import com.star.starboot.system.entity.UsersReRoles;
import com.star.starboot.system.service.UsersReRolesService;
import com.sun.management.OperatingSystemMXBean;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import sun.management.ManagementFactoryHelper;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统配置 前端控制器
 * </p>
 *
 * @author xpy
 * @since 2020-07-05
 */
@RestController
@RequestMapping("/systemSetting")
@CrossOrigin
@Slf4j
public class SystemSettingController extends AbstractController {


    @Value("${starboot.file.root}")
    private String fjPath;

    /**
     * SpringBoot获取当前环境代码,Spring获取当前环境代码
     */
    @Value("${spring.profiles.active}")
    private String profiles;

    /**
     * SpringBoot获取当前版本号
     */
    @Value("${starboot.starboot-version}")
    private String starbootVersion;

    /**
     * 获取服务器配置信息
     * @return
     */
    @ApiOperation(value = "系统配置-获取服务器配置信息", notes = "系统配置-获取服务器配置信息")
    @PostMapping("/getSystemConfig")
    @RequiresPermissions("sysLog_querySystemConfig")
    @SysLog(description = "系统配置-获取服务器配置信息")
    public Result sysLog_querySystemConfig(){
        // 版本号
        Map<String, String> result = new HashMap<>();
        result.put("starbootVersion", starbootVersion);

        // 运行的配置文件
        String activeProfile = ContextUtils.getApplicationContext().getEnvironment().getActiveProfiles()[0];
        result.put("activeProfile", activeProfile);

        // 系统版本
        OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactoryHelper.getOperatingSystemMXBean();
        String osName = operatingSystemMXBean.getName() + " " + operatingSystemMXBean.getArch() + " " + operatingSystemMXBean.getVersion();
        result.put("osName", osName);

        // 物理内存
        long totalPhysicalMemorySize = operatingSystemMXBean.getTotalPhysicalMemorySize()/1024/1024/1024;
        result.put("totalPhysicalMemorySize", totalPhysicalMemorySize+"GB");

        // jvm
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        result.put("jvmVersion", runtimeMXBean.getVmName() + " " + runtimeMXBean.getVmVersion());

        // JDK版本
        String jdkVersion = System.getProperty("java.version");
        result.put("jdkVersion", jdkVersion);

        // JDK安装路径
        String jdkPath = System.getProperty("java.home");
        result.put("jdkPath", jdkPath);

        // 系统启动时间
        DateTime date = DateUtil.date(runtimeMXBean.getStartTime());
        result.put("lastStartTime", date.toString());

        // 系统运行时间
        String formatBetween = DateUtil.formatBetween(runtimeMXBean.getUptime(), BetweenFormater.Level.SECOND);
        result.put("runTime", formatBetween);

        // 上传附件文件夹路径
        result.put("fjPath", fjPath);

        // 上传附件文件夹大小
        long size = FileUtil.size(new File(fjPath));
        result.put("fjSize", size/1024/1024 + "MB");

        // 系统jar包文件夹路径
        String path = System.getProperty("java.class.path");
        int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
        int lastIndex = path.lastIndexOf(File.separator) + 1;
        String jarPath = path.substring(firstIndex, lastIndex);
        result.put("jarPath", jarPath);

        // 当前jar文件名
        String jarName = path.substring(lastIndex);
        result.put("jarName", jarName);
        return Result.success(result);
    }
}

