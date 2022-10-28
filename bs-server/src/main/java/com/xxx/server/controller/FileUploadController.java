package com.xxx.server.controller;

import com.xxx.server.pojo.FileUploadResponseDTO;
import com.xxx.server.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/upload")
    public FileUploadResponseDTO upload(MultipartFile file) throws Exception {
        FileUploadResponseDTO fileUploadResponseDTO = fileUploadService.uploadResponseDTO(file);
        return fileUploadResponseDTO;
    }
}