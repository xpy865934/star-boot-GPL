package com.star.starboot.config.flowable;

import com.star.starboot.system.service.FirstDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
public class FlowableUserLoginController {

    @Autowired
    private FirstDictService firstDictService;

    @RequestMapping("/flowableAdminLogin")
    public void flowableAdminLogin(String userName, String password, HttpServletRequest request, HttpServletResponse response) {
        try {
            // 从数据字典中读取账号密码
            Map<String, String> flowableAdmin = firstDictService.getFirstDictByShzjCode("flowableAdmin");
            if (flowableAdmin.get("userName").equals(userName) && flowableAdmin.get("password").equals(password)) {
                request.getServletContext().setAttribute("flowableAdmin", flowableAdmin.get("userName"));
                response.sendRedirect(request.getContextPath() + "/static/index.html");
            }
            //直接重定向到登录页面
            response.sendRedirect(request.getContextPath() + "/static/login/index.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/flowableAdminLogout")
    public void flowableAdminLogin(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getServletContext().setAttribute("flowableAdmin", "");
            response.sendRedirect(request.getContextPath() + "/static/login/index.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
