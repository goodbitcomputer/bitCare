package com.bit.bitcare.service;

import com.bit.bitcare.model.DeptDTO;
import com.bit.bitcare.model.EmployeeDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface EmployeeService {
    public ResponseEntity<String> getDeptAll() throws JsonProcessingException;
    public ResponseEntity<String> autoLogin(HttpServletRequest request) throws IOException;
    public ResponseEntity<String> auth(EmployeeDTO attempt, HttpServletRequest request, HttpServletResponse response, boolean rememberMe) throws IOException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException;
    public ResponseEntity<String> selectAll() throws JsonProcessingException;
    ResponseEntity<String> getDept(int deptId) throws JsonProcessingException;
    public boolean validate(String username);
    public EmployeeDTO selectOne(int id);
    public void update(int employeeId, String role);
    public void delete(int id);
    public ResponseEntity<String> register(EmployeeDTO attempt) throws IOException;
    public ResponseEntity<String> getLogInInfo(HttpServletRequest request) throws IOException;
    public ResponseEntity<String> logOut(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException;
    public ResponseEntity<String> updateInfo(EmployeeDTO employeeDTO, String dept, HttpServletRequest request) throws IOException;
}
