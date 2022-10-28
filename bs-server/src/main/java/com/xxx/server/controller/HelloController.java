package com.xxx.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 *
 * @author jiangyong
 * @since 1.0.0
 */
@RestController
public class HelloController {
    @GetMapping("hello")
    public String hello(){
        return "hello";
    }
}
