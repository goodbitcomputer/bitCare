package com.bit.bitcare.controller;

import com.bit.bitcare.model.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    // /receive를 메시지를 받을 endpoint로 설정합니다.
    @MessageMapping("/receive/{sender}")
    // SocketHandler는 1) /receive에서 메시지를 받고, /send로 메시지를 보내줍니다.
    // 정의한 MessageDTO를 1) 인자값, 2) 반환값으로 사용합니다.
    public void SocketHandler(@DestinationVariable("sender") String send, MessageDTO messageDTO) {
        // vo에서 getter로 userName을 가져옵니다.
        String receiver = messageDTO.getReceiver();
        String content = messageDTO.getContent();
        String messageFile = messageDTO.getMessageFile();
        String state = messageDTO.getState();

        // 생성자로 반환값을 생성합니다.
        MessageDTO result = new MessageDTO(receiver, send, content, messageFile, state);
        // 반환
        simpMessagingTemplate.convertAndSend("/send/"+send, result);
    }
}