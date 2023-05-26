package com.bit.bitcare.controller;

import com.bit.bitcare.model.AlarmDTO;
import com.bit.bitcare.model.MessageDTO;
import com.bit.bitcare.service.AlarmService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class SocketController {

    /**
     * ---------------------------------------------------------------------------
     * 2023.05.16
     *
     * alarmService
     * AlarmService 객체 주입
     */
    @Autowired
    private AlarmService alarmService;

    /**
     * ---------------------------------------------------------------------------
     * 2023.05.16
     *
     * SocketHandler
     * /receive 주소로 메시지를 받고 alarmService.SocketHandler 메소드를 이용하여 /send 주소로 메시지를 보냄
     *
     */
    @MessageMapping("/receive/{receive}")
    // SocketHandler는 1) /receive에서 메시지를 받고, /send로 메시지를 보내줍니다.
    // 정의한 MessageDTO를 1) 인자값, 2) 반환값으로 사용합니다.
    public void SocketHandler(@DestinationVariable("receive") String send, MessageDTO messageDTO, AlarmDTO alarmDTO,String payload) throws JsonProcessingException {
        alarmService.SocketHandler(send,messageDTO,alarmDTO,payload);
    }
}