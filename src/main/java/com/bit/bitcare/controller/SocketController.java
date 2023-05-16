package com.bit.bitcare.controller;

import com.bit.bitcare.model.AlarmDTO;
import com.bit.bitcare.model.MessageDTO;
import com.bit.bitcare.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {

    @Autowired
    private AlarmService alarmService;

    // /receive를 메시지를 받을 endpoint로 설정합니다.
    @MessageMapping("/receive/{receive}")
    // SocketHandler는 1) /receive에서 메시지를 받고, /send로 메시지를 보내줍니다.
    // 정의한 MessageDTO를 1) 인자값, 2) 반환값으로 사용합니다.
    public void SocketHandler(@DestinationVariable("receive") String send, MessageDTO messageDTO, AlarmDTO alarmDTO) {
        alarmService.SocketHandler(send,messageDTO,alarmDTO);
    }
}