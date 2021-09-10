package org.cxw.vhr.controller.chat;

import org.cxw.vhr.model.ChatMessage;
import org.cxw.vhr.model.Hr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
public class WsController {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/ws/chat")
    public void handleMsg(Authentication authentication, ChatMessage chatMessage) {
        Hr hr = (Hr) authentication.getPrincipal();
        chatMessage.setFrom(hr.getUsername());
        chatMessage.setFromNickName(hr.getName());
        chatMessage.setTime(new Date());
        simpMessagingTemplate.convertAndSendToUser(chatMessage.getTo(), "/queue/chat", chatMessage);
    }
}
