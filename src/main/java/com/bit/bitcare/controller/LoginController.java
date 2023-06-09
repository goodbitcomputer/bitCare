package com.bit.bitcare.controller;

import com.bit.bitcare.model.EmployeeDTO;
import com.bit.bitcare.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**---------------------------------------------------------------------------
     * 2023.05.25
     *
     * selectAll
     * /api/selectAll 에 대한 Get 요청시 employeeService.selectAll 메소드를 실행
     * employeeService.selectAll 메소드에선 관리자를 제외한 모든 직원의 정보를 리턴
     */
    @GetMapping("/api/selectAll")
    public ResponseEntity<String> selectAll() throws IOException {
        // 관리자를 제외한 모든 직원의 정보를 받아옴
        return employeeService.selectAll();
    }

    /**---------------------------------------------------------------------------
     * 2023.06.22.
     *
     * selectAll
     * /api/selectAllRole 에 대한 Get 요청시 employeeService.selectAllRole 메소드를 실행
     * employeeService.selectAllRole 메소드에선 모든 직원의 정보를 리턴
     */
    @GetMapping("/api/selectAllRole")
    public ResponseEntity<String> selectAllRole() throws IOException {
        // 모든 직원의 정보를 받아옴
        return employeeService.selectAllRole();
    }

    /**---------------------------------------------------------------------------
     * 2023.05.26
     *
     * updateEmployee
     * /api/updateEmployee 에 대한 Get 요청시 employeeService.update 메소드를 실행
     * requestParam 으로 받은 EmployeeDTO 의 id 값과 role 을 통해 계정의 권한을 바꿈
     */
    @GetMapping("/api/updateEmployee")
    public void updateEmployee(@RequestParam int id, @RequestParam String role) throws IOException {
        // 권한 변경
        employeeService.update(id,role);
    }

    /**---------------------------------------------------------------------------
     * 2023.05.30
     *
     * getDeptList
     * /api/getDeptList 에 대한 Get 요청시 employeeService.getDeptAll 메소드를 실행
     * deptList(진료과목 목록)을 리턴함
     */
    @GetMapping("/api/getDeptList")
    public ResponseEntity<String> getDeptList() throws IOException {
        return employeeService.getDeptAll();
    }

    /**---------------------------------------------------------------------------
     * 2023.05.30
     *
     * register
     * /api/getDeptList 에 대한 Post 요청시 employeeService.register 메소드를 실행
     * 계정 추가 로직을 실행함
     */
    @PostMapping("/api/register")
    public ResponseEntity<String> register(EmployeeDTO attempt) throws IOException {
        return employeeService.register(attempt);
    }

    /**
     * ---------------------------------------------------------------------------
     * 2023.06.02
     *
     * logOut
     * /logOut 에 대한 Post 요청시 employeeService.logOut 메소드를 실행
     * employeeService.logOut 메소드에선 로그아웃 로직을 실행함
     */
    @PostMapping("/logOut")
    public ResponseEntity<String> logOut(HttpServletRequest request, HttpServletResponse response) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        return employeeService.logOut(request,response);
    }

    /**
     * ---------------------------------------------------------------------------
     * 2023.06.07
     *
     * dept
     * /api/dept 에 대한 Get 요청시 employeeService.getDept 메소드를 실행
     * employeeService.getDept 메소드에선 id에 해당하는 진료과목명을 리턴함
     */
    @GetMapping("/api/dept")
    public ResponseEntity<String> dept(@RequestParam int deptId) throws IOException{
        return employeeService.getDept(deptId);
    }

    /**
     * ---------------------------------------------------------------------------
     * 2023.06.08
     *
     * update
     * /api/infoUpdate 에 대한 Get 요청시 employeeService.updateInfo 메소드를 실행
     * employeeService.updateInfo 메소드에선 입력된 수정 정보를 업데이트함
     */
    @PostMapping("/api/infoUpdate")
    public ResponseEntity<String> update(EmployeeDTO employeeDTO, String dept, HttpServletRequest request) throws IOException{
        return employeeService.updateInfo(employeeDTO, dept, request);
    }
}
