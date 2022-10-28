package com.xxx.server.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
 * @since 2022-02-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_help_information")
@ApiModel(value="HelpInformation对象", description="")
public class HelpInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "求助信息id")
    @TableId(type = IdType.ID_WORKER)
    @JsonSerialize(using= ToStringSerializer.class)
    private Long helpId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "Asia/Shanghai")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "求助信息")
    private String helpInformation;

    @ApiModelProperty(value = "求助信息主题")
    private String helpTitle;

    @ApiModelProperty(value = "头像")
    private String userFace;

    @ApiModelProperty(value = "求助内容介绍")
    private String helpIntroduce;
}
