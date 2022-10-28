package com.xxx.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxx.server.pojo.HelpComment;
import com.xxx.server.pojo.HelpInformation;
import com.xxx.server.pojo.RespBean;
import com.xxx.server.pojo.ShareComment;
import com.xxx.server.service.IAdminService;
import com.xxx.server.service.IHelpCommentService;
import com.xxx.server.service.IShareCommentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jiangyong
 * @since 2022-04-07
 */
@RestController
@RequestMapping("/help-comment")
public class HelpCommentController {

    @Autowired
    private IHelpCommentService helpCommentService;
    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "获取当前信息的评论信息")
    @GetMapping("/gainComment/{helpId}")
    public List<HelpComment> getAdminHelpInformations(@PathVariable Long helpId){
        QueryWrapper<HelpComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("helpId",helpId);
        return helpCommentService.list(queryWrapper);
    }
    @ApiOperation(value = "获取所有求助评论")
    @GetMapping("/gain")
    public List<HelpComment> getAllHelpComment(){
        return helpCommentService.list();
    }

    @ApiOperation(value = "求助评论总数")
    @GetMapping("/gainSum")
    public Integer getSum(){
        return helpCommentService.count();
    }

    @ApiOperation(value = "添加评论信息")
    @PostMapping("/addComment")
    public RespBean addHelpComment(@RequestBody HelpComment helpComment){
        helpComment.setCommentCreateTime(LocalDateTime.now());
        if(helpCommentService.save(helpComment)){
            return RespBean.success("评论成功！");
        }
        return RespBean.error("评论失败！");
    }

    @ApiOperation(value = "管理员添加求助评论")
    @PostMapping("/add")
    public RespBean add(@RequestBody HelpComment helpComment){
        String userName = helpComment.getCommentator();
        String userFace = adminService.getAdminByUserName(userName).getUserFace();
        helpComment.setUserFace(userFace);
        helpComment.setCommentCreateTime(LocalDateTime.now());
        if(null==helpCommentService.getById(helpComment.getCommentId())&&helpCommentService.save(helpComment)){
            return RespBean.success("添加成功！");
        }
        return RespBean.error("添加失败！");
    }

    @ApiOperation(value = "删除求助评论")
    @DeleteMapping("/{id}")
    public RespBean deleteHelpComment(@PathVariable Long id){
        if(helpCommentService.removeById(id)){
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败！");
    }

    @ApiOperation(value = "更新求助评论")
    @PutMapping("/updateComment")
    public RespBean updateHelpComment(@RequestBody HelpComment helpComment){
        if(helpCommentService.updateById(helpComment)){
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");
    }

    @ApiOperation(value = "搜索求助评论")
    @GetMapping("/search/{keyword}")
    public List<HelpComment> search(@PathVariable String keyword){
        QueryWrapper<HelpComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("commentator",keyword);
        return helpCommentService.list(queryWrapper);
    }

}
