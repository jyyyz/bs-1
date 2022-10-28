package com.xxx.server.pojo;

import lombok.Data;

@Data
public class FileUploadResponseDTO {
    private String url;
    private String md5;
    private String path;
    private String domain;
    private String scene;
    private Long size;
    private Long mtime;
    private String scenes;
    private String retmsg;
    private Integer retcode;
    private String src;
}