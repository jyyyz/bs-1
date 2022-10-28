package com.xxx.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxx.server.mapper.AdminMapper;
import com.xxx.server.pojo.Admin;
import com.xxx.server.pojo.HelpInformation;
import com.xxx.server.pojo.RespBean;
import com.xxx.server.service.IAdminService;
import io.swagger.annotations.ApiOperation;
import javassist.compiler.ast.Keyword;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jiangyong
 * @since 2022-02-09
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "获取所有操作员")
    @GetMapping("/")
    public List<Admin> getAllAdmins(String keywords){
        return adminService.getAllAdmins(keywords);
    }

    @ApiOperation(value = "获取所有用户")
    @GetMapping("/getUser")
    public List<Admin> getAllUser(){
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("id","1");
        return adminService.list(queryWrapper);
    }

    @ApiOperation(value = "男用户数")
    @GetMapping("/gainSum1")
    public Integer getSum1(){
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("id",1).eq("sex",'男');
        return adminService.count(queryWrapper);
    }

    @ApiOperation(value = "女用户数")
    @GetMapping("/gainSum2")
    public Integer getSum2(){
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("id",1).eq("sex",'女');
        return adminService.count(queryWrapper);
    }

    @ApiOperation(value = "添加用户")
    @PostMapping("/addUser")
    public RespBean addAdmin(@RequestBody Admin admin){
        admin.setStatus(true);
        String password = admin.getPassword();
        password = new BCryptPasswordEncoder().encode(password);
        admin.setPassword(password);
        String username = admin.getUsername();
        if(null==adminService.getAdminByUserName(username)&&adminService.save(admin)){
            return RespBean.success("添加成功！");
        }
        return RespBean.error("添加失败,可能已存在该账号！");
    }

    @ApiOperation(value = "管理用户信息")
    @PutMapping("/updateUser")
    public RespBean updateAdmin(@RequestBody Admin admin){
        String password = admin.getPassword();
        password = new BCryptPasswordEncoder().encode(password);
        admin.setPassword(password);
        if(adminService.updateById(admin)){
            return RespBean.success("更改成功！");
        }
        return RespBean.error("更改失败！");
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("/User/{id}")
    public RespBean deleteUser(@PathVariable Integer id){
        if(adminService.removeById(id)){
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败！");
    }

    @ApiOperation(value = "搜索用户")
    @GetMapping("/search/{keyword}")
    public List<Admin> search(@PathVariable String keyword){
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",keyword);
        return adminService.list(queryWrapper);
    }

    @ApiOperation(value = "用户账号状态")
    @PutMapping("/status/{id}")
    public RespBean updateStatus(@PathVariable Integer id){
        Admin admin = adminService.getById(id);
        boolean status = admin.isEnabled();
        admin.setStatus(!status);
        if(adminService.updateById(admin)){
            return RespBean.success("修改成功！");
        }
        return RespBean.error("修改失败！");
    }
}
