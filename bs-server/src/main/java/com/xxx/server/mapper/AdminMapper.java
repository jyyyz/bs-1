package com.xxx.server.mapper;

import com.xxx.server.pojo.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxx.server.pojo.Admin;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jiangyong
 * @since 2022-02-09
 */
public interface AdminMapper extends BaseMapper<Admin> {

    //获取所有操作员
    List<Admin> getAllAdmins(@Param("id") Integer id, @Param("keywords") String keywords);

}
