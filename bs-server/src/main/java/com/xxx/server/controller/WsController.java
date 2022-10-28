package com.xxx.server.controller;

import com.xxx.server.pojo.Admin;
import com.xxx.server.pojo.ChatMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

/**
 * websocket
 *
 * @author jiangyong
 * @since 1.0.0
 */
@Controller
public class WsController {
    @Autowired
    private SimpMessagingTemplate simpleMessageingTemplate;
    @MessageMapping("/ws/chat")
    public void handleMsg(Authentication authentication, ChatMsg chatMsg){
        //获取当前登录用户的对象
        Admin admin = (Admin) authentication.getPrincipal();
        chatMsg.setFrom(admin.getUsername());
        chatMsg.setFormNickName(admin.getName());
        chatMsg.setDate(LocalDateTime.now());
        simpleMessageingTemplate.convertAndSendToUser(chatMsg.getTo(),"/queue/chat",chatMsg);
    }
}

