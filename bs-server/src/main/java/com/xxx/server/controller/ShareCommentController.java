package com.xxx.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxx.server.pojo.*;
import com.xxx.server.service.IAdminService;
import com.xxx.server.service.IShareCommentService;
import com.xxx.server.service.IShareInformationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
@RequestMapping("/share-comment")
public class ShareCommentController {
    @Autowired
    private IShareCommentService shareCommentService;
    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "获取当前信息的评论信息")
    @GetMapping("/gainComment/{shareId}")
    public List<ShareComment> getAdminShareInformations(@PathVariable Long shareId){
        QueryWrapper<ShareComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shareId",shareId);
        return shareCommentService.list(queryWrapper);
    }

    @ApiOperation(value = "获取所有分享评论")
    @GetMapping("/gain")
    public List<ShareComment> getAllHelpComment(){
        return shareCommentService.list();
    }

    @ApiOperation(value = "分享评论总数")
    @GetMapping("/gainSum")
    public Integer getSum(){
        return shareCommentService.count();
    }


    @ApiOperation(value = "添加评论信息")
    @PostMapping("/addComment")
    public RespBean addShareComment(@RequestBody ShareComment shareComment){
        shareComment.setCommentCreateTime(LocalDateTime.now());
        if(shareCommentService.save(shareComment)){
            return RespBean.success("评论成功！");
        }
        return RespBean.error("评论失败！");
    }

    @ApiOperation(value = "管理员添加分享评论")
    @PostMapping("/add")
    public RespBean add(@RequestBody ShareComment shareComment){
        String userName = shareComment.getCommentator();
        String userFace = adminService.getAdminByUserName(userName).getUserFace();
        shareComment.setUserFace(userFace);
        shareComment.setCommentCreateTime(LocalDateTime.now());
        if(null==shareCommentService.getById(shareComment.getCommentId())&&shareCommentService.save(shareComment)){
            return RespBean.success("添加成功！");
        }
        return RespBean.error("添加失败！");
    }

    @ApiOperation(value = "删除分享评论")
    @DeleteMapping("/{id}")
    public RespBean deleteShareComment(@PathVariable Long id){
        if(shareCommentService.removeById(id)){
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败！");
    }

    @ApiOperation(value = "更新分享评论")
    @PutMapping("/updateComment")
    public RespBean updateShareComment(@RequestBody ShareComment shareComment){
        if(shareCommentService.updateById(shareComment)){
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");
    }

    @ApiOperation(value = "搜索分享评论")
    @GetMapping("/search/{keyword}")
    public List<ShareComment> search(@PathVariable String keyword){
        QueryWrapper<ShareComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("commentator",keyword);
        return shareCommentService.list(queryWrapper);
    }
}
