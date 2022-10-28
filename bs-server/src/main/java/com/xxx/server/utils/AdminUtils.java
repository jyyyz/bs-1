package com.xxx.server.utils;

import com.xxx.server.pojo.Admin;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 操作员工具类
 *
 * @author jiangyong
 * @since 1.0.0
 */
public class AdminUtils {
    //获取当前操作员
    public static Admin getCurrentAdmin(){
        return (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
