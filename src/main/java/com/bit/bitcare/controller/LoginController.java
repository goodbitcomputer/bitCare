package com.bit.bitcare.controller;

import com.bit.bitcare.model.EmployeeDTO;
import com.bit.bitcare.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
public class LoginController {

    /**
     * ---------------------------------------------------------------------------
     * 2023.05.16
     *
     * employeeService
     * EmployeeService 객체 주입
     * ---------------------------------------------------------------------------
     * 2023.05.17
     *
     * Controller 분리
     */
    @Autowired
    private EmployeeService employeeService;

    /**
     * ---------------------------------------------------------------------------
     * 2023.05.16
     *
     * login
     * /autoLogin 에 대한 Post 요청시 employeeService.autoLogin 메소드를 실행한 뒤 결과값 리턴
     * employeeService.autoLogin 메소드에선 자동 로그인 로직을 실행함
     * ---------------------------------------------------------------------------
     * 2023.05.17
     *
     * Controller 분리
     */
    @PostMapping("/autoLogin")
    public ResponseEntity<String> login(HttpServletRequest request) throws IOException {
        return employeeService.autoLogin(request);
    }

    /**
     * ---------------------------------------------------------------------------
     * 2023.05.16
     *
     * getLoginInfo
     * /api/login 에 대한 Get 요청시 employeeService.getLogInInfo 메소드를 실핸한 뒤 결과값 리턴
     * employeeService.getLogInInfo 메소드에선 세션에서 로그인 정보를 가져옴
     * ---------------------------------------------------------------------------
     * 2023.05.17
     *
     * Controller 분리
     */
    @GetMapping("/api/login")
    public ResponseEntity<String> getLoginInfo(HttpServletRequest request) throws IOException {
        // 세션에서 로그인 정보 가져오기
        return employeeService.getLogInInfo(request);
    }

    /**
     * ---------------------------------------------------------------------------
     * 2023.05.16
     *
     * auth
     * /auth 에 대한 Post 요청시 employeeService.auth 메소드를 실행
     * employeeService.auth 메소드에선 로그인 로직을 실행함
     * ---------------------------------------------------------------------------
     * 2023.05.17
     *
     * Controller 분리
     */
    @PostMapping("/auth")
    public ResponseEntity<String> auth(@RequestBody EmployeeDTO employeeDTO, boolean rememberMe, HttpServletRequest request, HttpServletResponse response) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        return employeeService.auth(employeeDTO,request,response,rememberMe);
    }
}
