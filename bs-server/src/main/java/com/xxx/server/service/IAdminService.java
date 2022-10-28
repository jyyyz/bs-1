package com.xxx.server.service;

import com.xxx.server.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.server.pojo.RespBean;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jiangyong
 * @since 2022-02-09
 */
public interface IAdminService extends IService<Admin> {
    //登录之后返回token
    RespBean login(String username, String password,String code, HttpServletRequest request);
    //根据用户名获取用户
    Admin getAdminByUserName(String username);
    //获取所有操作员
    List<Admin> getAllAdmins(String keywords);
    //更新用户密码
    RespBean updateAdminPassword(String oldPass, String pass, Integer id);
    //更新用户头像
    RespBean updateAdminUserFace(String url, Integer id, Authentication authentication);
}
