package com.xxx.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxx.server.mapper.HelpInformationMapper;
import com.xxx.server.mapper.ShareInformationMapper;
import com.xxx.server.pojo.Admin;
import com.xxx.server.pojo.HelpInformation;
import com.xxx.server.pojo.RespBean;
import com.xxx.server.pojo.ShareInformation;
import com.xxx.server.service.IAdminService;
import com.xxx.server.service.IHelpInformationService;
import com.xxx.server.service.IShareInformationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jiangyong
 * @since 2022-02-25
 */
@RestController
@RequestMapping("/help-information")
public class HelpInformationController {
    @Autowired
    private IHelpInformationService helpInformationService;
    @Autowired
    private IAdminService adminService;

    @Resource
    private HelpInformationMapper mapper;

    @ApiOperation(value = "获取所有求助信息")
    @GetMapping("/gain")
    public List<HelpInformation> getAllHelpInformations(){
        return helpInformationService.list();
    }

    @ApiOperation(value = "求助总数")
    @GetMapping("/gainSum")
    public Integer getSum(){
        return helpInformationService.count();
    }

    @ApiOperation(value = "获取指定id求助信息")
    @GetMapping("/gain/{id}")
    public Object getHelpInformations(@PathVariable String id){
        QueryWrapper<HelpInformation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("helpId",id);
        return mapper.selectOne(queryWrapper);
    }

    @ApiOperation(value = "获取当前用户的求助信息")
    @GetMapping("/userHelp")
    public List<HelpInformation> getAdminHelpInformations(Principal principal){
        QueryWrapper<HelpInformation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",principal.getName());
        return helpInformationService.list(queryWrapper);
    }

    @ApiOperation(value = "添加求助信息")
    @PostMapping("/addHelp")
    public RespBean addHelpInformation(@RequestBody HelpInformation helpInformation, Principal principal, Authentication authentication){
        helpInformation.setUsername(principal.getName());
        Admin admin = (Admin) authentication.getPrincipal();
        helpInformation.setUserFace(admin.getUserFace());
        helpInformation.setCreateDate(LocalDateTime.now());
        if(helpInformationService.save(helpInformation)){
            return RespBean.success("发布成功！");
        }
        return RespBean.error("发布失败！");
    }

    @ApiOperation(value = "管理员添加求助")
    @PostMapping("/add")
    public RespBean addShare(@RequestBody HelpInformation helpInformation){
        String userName = helpInformation.getUsername();
        String userFace = adminService.getAdminByUserName(userName).getUserFace();
        helpInformation.setUserFace(userFace);
        helpInformation.setCreateDate(LocalDateTime.now());
        if(null==helpInformationService.getById(helpInformation.getHelpId())&&helpInformationService.save(helpInformation)){
            return RespBean.success("添加成功！");
        }
        return RespBean.error("添加失败！");
    }


    @ApiOperation(value = "更新求助信息")
    @PutMapping("/updateHelp")
    public RespBean updateHelpInformation(@RequestBody HelpInformation helpInformation){
        if(helpInformationService.updateById(helpInformation)){
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");
    }
    @ApiOperation(value = "删除求助信息")
    @DeleteMapping("/{id}")
    public RespBean deleteHelpInformation(@PathVariable Long id){
        if(helpInformationService.removeById(id)){
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败！");
    }

    @ApiOperation(value = "批量删除求助信息")
    @DeleteMapping("/")
    public RespBean deleteHelpInformationsByIds(Integer[] ids){
        if (helpInformationService.removeByIds(Arrays.asList(ids))){
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败！");
    }

    @ApiOperation(value = "搜索求助")
    @GetMapping("/search/{keyword}")
    public List<HelpInformation> search(@PathVariable String keyword){
        QueryWrapper<HelpInformation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",keyword);
        return helpInformationService.list(queryWrapper);
    }

}
