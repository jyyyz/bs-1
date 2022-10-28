package com.xxx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xxx.server.config.JwtTokenUtil;
import com.xxx.server.utils.AdminUtils;
import com.xxx.server.pojo.Admin;
import com.xxx.server.mapper.AdminMapper;
import com.xxx.server.pojo.RespBean;
import com.xxx.server.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jiangyong
 * @since 2022-02-09
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    //private PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHead}")
    private String tokenHead;


    //登录之后返回token
    @Override
    public RespBean login(String username, String password, String code, HttpServletRequest request) {
        String captcha = (String) request.getSession().getAttribute("captcha");
        if(StringUtils.isEmpty(code)||!captcha.equalsIgnoreCase(code)){
            return RespBean.error("验证码输入错误，请重新输入！");
        }
        //登录
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        //对数据库密码进行加密
        //String encodedPassword = passwordEncoder.encode(userDetails.getPassword());
        if(null==userDetails||!passwordEncoder.matches(password,userDetails.getPassword())){
            return RespBean.error("用户名或密码错误！");
        }
        if(!userDetails.isEnabled()){
            return RespBean.error("账号被禁用，请联系管理员！");
        }
        //更新security登录用户对象
        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //生成token
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenHead",tokenHead);
        return RespBean.success("登录成功",tokenMap);
    }

    /*@Override
    public RespBean register(Admin admin, HttpServletRequest request) {
        admin.setEnabled(true);
        adminMapper.register(admin);
        if(1==admin.getResult()){
            return RespBean.success("注册成功！",admin);
        }
        return RespBean.error("注册失败");
    }*/
    /*private void encryptPassword(Admin admin){
        String password = admin.getPassword();
        password = new BCryptPasswordEncoder().encode(password);
        admin.setPassword(password);
    }*/

    //根据用户名获取用户
    @Override
    public Admin getAdminByUserName(String username) {
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username",username).eq("status",true));
    }
    //获取所有操作员
    @Override
    public List<Admin> getAllAdmins(String keywords) {

        return adminMapper.getAllAdmins(AdminUtils.getCurrentAdmin().getId(),keywords);
    }
    //更新用户密码
    @Override
    public RespBean updateAdminPassword(String oldPass, String pass, Integer id) {
        Admin admin = adminMapper.selectById(id);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //判断旧密码是否正确
        if (encoder.matches(oldPass,admin.getPassword())){
            admin.setPassword(encoder.encode(pass));
            int result = adminMapper.updateById(admin);
            if (1==result){
                return RespBean.success("更新成功！");
            }
            return RespBean.error("更新失败！");
        }
        return RespBean.error("旧密码输入错误！");
    }

    //更新用户头像
    @Override
    public RespBean updateAdminUserFace(String url, Integer id, Authentication authentication) {
        Admin admin = adminMapper.selectById(id);
        admin.setUserFace(url);
        int result = adminMapper.updateById(admin);
        if (1==result){
            Admin principal = (Admin) authentication.getPrincipal();
            principal.setUserFace(url);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(admin,null,authentication.getAuthorities()));
            return RespBean.success("更新头像成功！",url);
        }
        return RespBean.error("更新头像失败！");
    }
}
