package com.xxx.server.controller;

import com.xxx.server.mapper.AdminMapper;
import com.xxx.server.pojo.Admin;
import com.xxx.server.pojo.AdminLoginParam;
import com.xxx.server.pojo.RespBean;
import com.xxx.server.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * 登录
 *
 * @author jiangyong
 * @since 1.0.0
 */
@Api(tags = "LoginController")
@RestController
public class LoginController {
    @Autowired
    private IAdminService adminService;

    private AdminMapper adminMapper;

    @ApiOperation(value = "登录之后返回token")
    @PostMapping("/login")
    public RespBean login(@RequestBody AdminLoginParam adminLoginParam, HttpServletRequest request){
        return adminService.login(adminLoginParam.getUsername(),adminLoginParam.getPassword(),adminLoginParam.getCode(),request);
    }
    /*@ApiOperation(value = "注册")
    @PostMapping("/register")
    public RespBean addAdmin(@RequestBody Admin admin){
        if(adminService.save(admin)){
            return RespBean.success("注册成功！");
        }
        return RespBean.error("注册失败！");
    }*/

    @ApiOperation(value = "获取当前登录用户的信息")
    @GetMapping("/admin/info")
    public Admin getAdminInfo(Principal principal){
        if(null == principal){
            return null;
        }
        String username = principal.getName();
        Admin admin = adminService.getAdminByUserName(username);
        admin.setPassword(null);
        //admin.setRoles(adminService.getRoles(admin.getId()));
        return admin;
    }

    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public RespBean logout() {
        return RespBean.success("注销成功!");
    }
}
