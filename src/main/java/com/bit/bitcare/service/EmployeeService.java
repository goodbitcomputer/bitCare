package com.bit.bitcare.service;

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
    public ResponseEntity<String> autoLogin(HttpServletRequest request) throws IOException;
    public ResponseEntity<String> auth(EmployeeDTO attempt, HttpServletRequest request, HttpServletResponse response, boolean rememberMe) throws IOException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException;
    public List<EmployeeDTO> selectAll();
    public boolean validate(String username);
    public EmployeeDTO selectOne(int id);
    public void update(EmployeeDTO employeeDTO);
    public void delete(int id);
    public boolean register(EmployeeDTO attempt);
    public ResponseEntity<String> getLogInInfo(HttpServletRequest request) throws IOException;
}
