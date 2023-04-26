package com.bit.bitcare.controller;

import com.bit.bitcare.model.MessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {

    // /receive를 메시지를 받을 endpoint로 설정합니다.
    @MessageMapping("/receive")

    // /send로 메시지를 반환합니다.
    @SendTo("/send")

    // SocketHandler는 1) /receive에서 메시지를 받고, /send로 메시지를 보내줍니다.
    // 정의한 MessageDTO를 1) 인자값, 2) 반환값으로 사용합니다.
    public MessageDTO SocketHandler(MessageDTO messageDTO) {
        // vo에서 getter로 userName을 가져옵니다.
        String receiver = messageDTO.getReceiver();
        String sender = messageDTO.getSender();
        String content = messageDTO.getContent();
        String messageFile = messageDTO.getMessageFile();
        String state = messageDTO.getState();

        // 생성자로 반환값을 생성합니다.
        MessageDTO result = new MessageDTO(receiver, sender, content, messageFile, state);
        // 반환
        return result;
    }
}