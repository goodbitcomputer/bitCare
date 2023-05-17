package com.bit.bitcare.controller;

import com.bit.bitcare.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class AlarmController {

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
     * getReceiveList
     * /api/receiveList 에 대한 Get 요청시 alarmService.getReceiveList 메소드를 실행한 뒤 결과값 리턴
     * alarmService.getReceiveList 메소드에선 신규 알람 목록을 가져옴
     */
    @GetMapping("/api/receiveList")
    public ResponseEntity<String> getReceiveList(HttpServletRequest request) throws IOException {
        return alarmService.getReceiveList(request);
    }

    /**
     * ---------------------------------------------------------------------------
     * 2023.05.16
     *
     * deleteMessage
     * /api/deleteMessage 에 대한 Get 요청시 alarmService.deleteMessage 메소드를 실행
     * alarmService.deleteMessage 메소드에선 알림 삭제 로직을 실행함
     */
    @GetMapping("/api/deleteMessage")
    public void deleteMessage(@RequestParam int id) throws IOException {
        // 선택 메세지 삭제
        alarmService.deleteMessage(id);
    }
}