package com.xxx.server.service;


import com.alibaba.fastjson.JSONObject;
import com.xxx.server.pojo.FileUploadResponseDTO;
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

/**
 * @author jiangyong
 * @since 1.0.0
 */
@Service
public class FileUploadService {
    public FileUploadResponseDTO uploadResponseDTO(MultipartFile file) throws Exception{
        String originalFilename = file.getOriginalFilename();
        OkHttpClient httpClient = (new OkHttpClient.Builder()).readTimeout(5L, TimeUnit.MINUTES).build();
        MultipartBody multipartBody = (new MultipartBody.Builder()).setType(MultipartBody.FORM).addFormDataPart("file", originalFilename, RequestBody.create(MediaType.parse("multipart/form-data;charset=utf-8"), file.getBytes())).addFormDataPart("output", "json").build();
        Request request = (new Request.Builder()).url("http://localhost:8080/group1/upload").post(multipartBody).build();
        Response response = httpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            ResponseBody body = response.body();
            if (body != null) {
                return (FileUploadResponseDTO) JSONObject.parseObject(body.string(), FileUploadResponseDTO.class);
            }
        }
        throw new Exception("上传资源失败");
    }
}
