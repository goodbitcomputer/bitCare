package com.bit.bitcare.controller;

import com.bit.bitcare.model.EmployeeDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LogInController {

    private final ObjectMapper objectMapper;

    public LogInController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @GetMapping("/api/login")
    public ResponseEntity<String> getLoginInfo(HttpServletRequest request) throws IOException {
        // 세션에서 로그인 정보 가져오기
        HttpSession session = request.getSession();
        EmployeeDTO employeeDTO = (EmployeeDTO) session.getAttribute("logIn");
        System.out.println(employeeDTO);
        boolean isLoggedIn = (employeeDTO != null);

        // JSON 데이터 생성
        Map<String, Object> data = new HashMap<>();
        data.put("isLoggedIn", isLoggedIn);
        data.put("logIn", employeeDTO);
        if (employeeDTO != null) {
            data.put("username", employeeDTO.getUsername());
        } else {
            data.put("username", null);
        }

        // JSON 문자열 생성
        String json = objectMapper.writeValueAsString(data);

        // HTTP 응답 생성
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }
}