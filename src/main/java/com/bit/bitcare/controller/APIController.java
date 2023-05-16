package com.bit.bitcare.controller;

import com.bit.bitcare.model.EmployeeDTO;
import com.bit.bitcare.service.AlarmService;
import com.bit.bitcare.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
public class APIController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AlarmService alarmService;

    @PostMapping("/autoLogin")
    public ResponseEntity<String> login(HttpServletRequest request) throws IOException{
        return employeeService.autoLogin(request);
    }

    @GetMapping("/api/login")
    public ResponseEntity<String> getLoginInfo(HttpServletRequest request) throws IOException {
        // 세션에서 로그인 정보 가져오기
        return employeeService.getLogInInfo(request);
    }

    @GetMapping("/api/receiveList")
    public ResponseEntity<String> getReceiveList(HttpServletRequest request) throws IOException {
        // 받은 메세지 목록 가져오기
        return alarmService.getReceiveList(request);
    }

    @GetMapping("/api/deleteMessage")
    public void deleteMessage(@RequestParam int id) throws IOException {
        // 선택 메세지 삭제
        alarmService.deleteMessage(id);
    }

    @PostMapping("/auth")
    public ResponseEntity<String> auth(@RequestBody EmployeeDTO employeeDTO, boolean rememberMe, HttpServletRequest request, HttpServletResponse response) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        return employeeService.auth(employeeDTO,request,response,rememberMe);
    }
}