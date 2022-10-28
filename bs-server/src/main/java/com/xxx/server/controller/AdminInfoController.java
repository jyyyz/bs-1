package com.xxx.server.controller;

import cn.hutool.core.io.resource.InputStreamResource;
import cn.hutool.core.lang.Console;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.xxx.server.pojo.Admin;
import com.xxx.server.pojo.FileUploadResponseDTO;
import com.xxx.server.pojo.RespBean;
import com.xxx.server.service.FileUploadService;
import com.xxx.server.service.IAdminService;
import com.xxx.server.utils.FastDFSUtils;
import io.swagger.annotations.ApiOperation;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 个人中心
 *
 * @author jiangyong
 * @since 1.0.0
 */
@RestController
public class AdminInfoController {
    @Autowired
    private IAdminService adminService;
    @Autowired
    private FileUploadService fileUploadService;

    @ApiOperation(value = "更新当前用户信息")
    @PutMapping("/admin/info")
    public RespBean updateAdmin(@RequestBody Admin admin, Authentication authentication){
        if (adminService.updateById(admin)){
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(admin,null,
                    authentication.getAuthorities()));
            return RespBean.success("更新成功！");
        }
        return  RespBean.error("更新失败！");
    }

    @ApiOperation(value = "更新用户密码")
    @PutMapping("/admin/pass")
    public RespBean updateAdminPassword(@RequestBody Map<String,Object> info, Principal principal){
        String oldPass = (String) info.get("oldPass");
        String pass = (String) info.get("pass");
        Admin admin = adminService.getAdminByUserName(principal.getName());
        Integer id = admin.getId();
//        Integer id = (Integer) info.get("id");
        return adminService.updateAdminPassword(oldPass,pass,id);
    }
    @ApiOperation(value = "更新用户头像")
    @PostMapping("/admin/userface")
    public RespBean updateAdminUserFace(MultipartFile file, Authentication authentication) throws Exception{
        FileUploadResponseDTO fileUploadResponseDTO = fileUploadService.uploadResponseDTO(file);
        String url = fileUploadResponseDTO.getUrl();
        url = url.substring(0,url.length()-1)+"0";
        /*String[] filePath = FastDFSUtils.upload(file);
        String url = FastDFSUtils.getTrackerUrl() + filePath[0] + "/" + filePath[1];*/
        Admin admin = (Admin) authentication.getPrincipal();
        Integer id = admin.getId();
    return adminService.updateAdminUserFace(url,id,authentication);
}

    @ApiOperation(value = "注册")
    @PostMapping("/admin/register")
    public RespBean addAdmin(@RequestBody Admin admin){
        admin.setStatus(true);
        String password = admin.getPassword();
        password = new BCryptPasswordEncoder().encode(password);
        admin.setPassword(password);
        String username = admin.getUsername();
        if(null==adminService.getAdminByUserName(username)&&adminService.save(admin)){
            return RespBean.success("注册成功！");
        }
        return RespBean.error("注册失败,可能已存在该账号！");
    }
}
