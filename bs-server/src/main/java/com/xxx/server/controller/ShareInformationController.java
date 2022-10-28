package com.xxx.server.controller;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxx.server.mapper.ShareInformationMapper;
import com.xxx.server.pojo.Admin;
import com.xxx.server.pojo.RespBean;
import com.xxx.server.pojo.ShareInformation;
import com.xxx.server.service.IAdminService;
import com.xxx.server.service.IShareInformationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jiangyong
 * @since 2022-02-25
 */
@RestController
@RequestMapping("/share-information")
public class ShareInformationController {

    @Autowired
    private IShareInformationService shareInformationService;
    @Autowired
    private IAdminService adminService;

    @Resource
    private ShareInformationMapper mapper;

    @ApiOperation(value = "获取所有分享信息")
    @GetMapping("/gain")
    public List<ShareInformation> getAllShareInformations(){
        return shareInformationService.list();
    }

    @ApiOperation(value = "分享总数")
    @GetMapping("/gainSum")
    public Integer getSum(){
        return shareInformationService.count();
    }

    @ApiOperation(value = "获取指定id分享信息")
    @GetMapping("/gain/{id}")
    public Object getShareInformations(@PathVariable String id){
        QueryWrapper<ShareInformation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shareId",id);
        return mapper.selectOne(queryWrapper);
    }
    @ApiOperation(value = "获取当前用户的分享信息")
    @GetMapping("/userShare")
    public List<ShareInformation> getAdminShareInformations(Principal principal){
        QueryWrapper<ShareInformation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",principal.getName());
        return shareInformationService.list(queryWrapper);
    }

    @ApiOperation(value = "添加分享信息")
    @PostMapping("/addShare")
    public RespBean addShareInformation(@RequestBody ShareInformation shareInformation, Principal principal, Authentication authentication){
        shareInformation.setUsername(principal.getName());
        Admin admin = (Admin) authentication.getPrincipal();
        shareInformation.setUserFace(admin.getUserFace());
        shareInformation.setCreateDate(LocalDateTime.now());
        if(shareInformationService.save(shareInformation)){
            return RespBean.success("分享成功！");
        }
        return RespBean.error("分享失败！");
    }

    @ApiOperation(value = "管理员添加分享")
    @PostMapping("/add")
    public RespBean addShare(@RequestBody ShareInformation shareInformation){
        String userName = shareInformation.getUsername();
        String userFace = adminService.getAdminByUserName(userName).getUserFace();
        shareInformation.setUserFace(userFace);
        shareInformation.setCreateDate(LocalDateTime.now());
        if(null==shareInformationService.getById(shareInformation.getShareId())&&shareInformationService.save(shareInformation)){
            return RespBean.success("添加成功！");
        }
        return RespBean.error("添加失败！");
    }


    @ApiOperation(value = "更新分享信息")
    @PutMapping("/updateShare")
    public RespBean updateShareInformation(@RequestBody ShareInformation shareInformation){
        if(shareInformationService.updateById(shareInformation)){
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");
    }
    @ApiOperation(value = "删除分享信息")
    @DeleteMapping("/{id}")
    public RespBean deleteShareInformation(@PathVariable Long id){
        if(shareInformationService.removeById(id)){
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败！");
    }

    @ApiOperation(value = "批量删除分享信息")
    @DeleteMapping("/")
    public RespBean deleteShareInformationsByIds(Integer[] ids){
        if (shareInformationService.removeByIds(Arrays.asList(ids))){
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败！");
    }

    @ApiOperation(value = "搜索分享")
    @GetMapping("/search/{keyword}")
    public List<ShareInformation> search(@PathVariable String keyword){
        QueryWrapper<ShareInformation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",keyword);
        return shareInformationService.list(queryWrapper);
    }

}
