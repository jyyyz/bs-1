package com.xxx.server.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author jiangyong
 * @since 2022-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_share_comment")
@ApiModel(value="ShareComment对象", description="")
public class ShareComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分享信息评论id")
    @TableId(value = "commentId", type = IdType.AUTO)
    private Long commentId;

    @ApiModelProperty(value = "分享信息id")
    private Long shareId;

    @ApiModelProperty(value = "评论者")
    private String commentator;

    @ApiModelProperty(value = "评论内容")
    private String commentBody;

    @ApiModelProperty(value = "评论时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "Asia/Shanghai")
    private LocalDateTime commentCreateTime;

    @ApiModelProperty(value = "头像")
    private String userFace;

    @ApiModelProperty(value = "父id")
    private Long parentId;

    @ApiModelProperty(value = "父用户名")
    private String parentUsername;


}
