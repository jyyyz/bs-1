package com.xxx.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;


/**
 * 启动类
 *
 * @author jiangyong
 * @since 1.0.0
 */
@SpringBootApplication
@MapperScan("com.xxx.server.mapper")
public class BsApplication {
    public static void main(String[] args) {
        SpringApplication.run(BsApplication.class,args);
    }
}
