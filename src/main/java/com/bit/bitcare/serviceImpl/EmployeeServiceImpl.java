package com.bit.bitcare.serviceImpl;

import com.bit.bitcare.dao.DeptDAO;
import com.bit.bitcare.dao.EmployeeDAO;
import com.bit.bitcare.model.DeptDTO;
import com.bit.bitcare.model.EmployeeDTO;
import com.bit.bitcare.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import javax.crypto.spec.SecretKeySpec;
/**
 * ---------------------------------------------------------------------------
 * 2023.05.16
 *
 * EmployeeServiceImpl
 * EmployeeService 인터페이스에 대한 구현체
 * ObjectMapper, EmployeeDAO, BCryptPasswordEncoder, EmployeeDetailService, PersistentTokenRepositoryImpl 객체 사용
 * 메소드 목록
 * 1. autoLogin : cookie 에 저장되어 있는 토큰 정보로 자동 로그인을 수행하는 메소드.
 * 2. auth : EmployeeDTO 객체로 UserDetails 객체를 생성하여 SpringSecurity 인증을 진행하고, session 에 로그인 정보를 저장하여 로그인 처리를 하는 메소드
 *           rememberMe 값이 true 라면 인증 토큰을 생성하여 persistentTokenRepository에 저장하고 cookie를 생성한다.
 * 3. getLogInInfo : session 에 저장되어있는 로그인 정보를 받아서 리턴하는 메소드
 * 4. selectAll : 모든 직원의 정보를 List 객체로 만들어 json 형태로 리턴하는 메소드
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final ObjectMapper objectMapper;

    private final EmployeeDAO employeeDAO;

    private final DeptDAO deptDAO;

    private final BCryptPasswordEncoder passwordEncoder; // password는 데이터베이스에 인코딩되어 저장되어야한다. (시큐리티에서 막음)
    private final EmployeeDetailService employeeDetailService;
    private final PersistentTokenRepositoryImpl persistentTokenRepository;

    public EmployeeServiceImpl(ObjectMapper objectMapper, EmployeeDAO employeeDAO, DeptDAO deptDAO, BCryptPasswordEncoder passwordEncoder,
                               EmployeeDetailService employeeDetailService,
                               PersistentTokenRepositoryImpl persistentTokenRepository) {
        this.objectMapper = objectMapper;
        this.employeeDAO = employeeDAO;
        this.deptDAO = deptDAO;
        this.passwordEncoder = passwordEncoder;
        this.employeeDetailService = employeeDetailService;
        this.persistentTokenRepository = persistentTokenRepository;
    }

    @Override
    public ResponseEntity<String> getDeptAll() throws JsonProcessingException {
        Map<String, Object> data = new HashMap<>();
        List<DeptDTO> list = deptDAO.selectAll();

        data.put("deptList",list);

        // JSON 문자열 생성
        String json = objectMapper.writeValueAsString(data);

        // HTTP 응답 생성
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    @Override
    public ResponseEntity<String> getDept(int deptId) throws JsonProcessingException {
        Map<String, Object> data = new HashMap<>();
        DeptDTO dept = deptDAO.selectOne(deptId);

        if(dept != null) {
            data.put("dept", dept.getDept());
        }

        // JSON 문자열 생성
        String json = objectMapper.writeValueAsString(data);

        // HTTP 응답 생성
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    @Override
    public boolean validate(String username) {
        return employeeDAO.validate(username) == null;
    }

    @Override
    public EmployeeDTO selectOne(int id) {
        return employeeDAO.selectOne(id);
    }

    @Override
    public ResponseEntity<String> selectAll() throws JsonProcessingException
    {
        Map<String, Object> data = new HashMap<>();
        List<EmployeeDTO> employeeList = employeeDAO.selectAll();
        for(EmployeeDTO i : employeeList){
            i.setPassword("");
        }
        data.put("employeeList", employeeList);

        // JSON 문자열 생성
        String json = objectMapper.writeValueAsString(data);

        // HTTP 응답 생성
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    @Override
    public ResponseEntity<String> selectAllRole() throws JsonProcessingException
    {
        Map<String, Object> data = new HashMap<>();
        List<EmployeeDTO> employeeList = employeeDAO.selectAllRole();
        for(EmployeeDTO i : employeeList){
            i.setPassword("");
        }
        data.put("employeeList", employeeList);

        // JSON 문자열 생성
        String json = objectMapper.writeValueAsString(data);

        // HTTP 응답 생성
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    @Override
    public void update(int employeeId, String role) {
        EmployeeDTO employeeDTO = employeeDAO.selectOne(employeeId);
        employeeDTO.setRole(role);
        employeeDAO.update(employeeDTO);
    }

    @Override
    public void delete(int id) {
        employeeDAO.delete(id);
    }

    @Override
    public ResponseEntity<String> autoLogin(HttpServletRequest request) throws IOException{

        Map<String, Object> data = new HashMap<>();

        EmployeeDTO logIn = null;

        // 클라이언트로부터 remember-me 쿠키를 읽어옴
        Cookie[] cookies = request.getCookies();
        String rememberMeCookieValue = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("remember-me")) {
                    rememberMeCookieValue = cookie.getValue();
                    break;
                }
            }
        }

        if (rememberMeCookieValue != null) {

            // 쿠키에서 시리즈와 토큰 값을 분리
            String[] cookieParts = rememberMeCookieValue.split(":");
            String username = cookieParts[0];
            String series = cookieParts[1];
            String tokenValue = cookieParts[2];

            // 데이터베이스에서 시리즈에 해당하는 토큰을 조회
            PersistentRememberMeToken token = persistentTokenRepository.getTokenForSeries(series);

            if (token != null && token.getTokenValue().equals(tokenValue)) {
                UserDetails userDetails = employeeDetailService.loadUserByUsername(username);
                logIn = employeeDAO.validate(username);
                // 토큰이 유효하면 해당 사용자를 인증 처리
                HttpSession session = request.getSession();
                session.setAttribute("logIn", logIn);
                Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

            if(logIn != null) {
                data.put("logIn", "success");
                data.put("role", logIn.getRole());
                data.put("name", logIn.getName());
            }
        }else{
            data.put("logIn", "failed");
        }

        // JSON 문자열 생성
        String json = objectMapper.writeValueAsString(data);

        // HTTP 응답 생성
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    @Override
    public ResponseEntity<String> logOut(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        HttpSession httpSession = request.getSession();
        EmployeeDTO employeeDTO = (EmployeeDTO) httpSession.getAttribute("logIn");

        Map<String, Object> data = new HashMap<>();

        Cookie[] cookies = request.getCookies();
        if ((cookies != null) || (employeeDTO != null)) { // 쿠키가 한개라도 있으면 실행
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0); // 유효시간을 0으로 설정
                response.addCookie(cookie); // 응답에 추가하여 만료시키기.
            }

            persistentTokenRepository.removeUserTokens(employeeDTO.getUsername());
            httpSession.invalidate();

            data.put("logOut","success");
        }else{
            data.put("logOut","failed");
        }

        // JSON 문자열 생성
        String json = objectMapper.writeValueAsString(data);

        // HTTP 응답 생성
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    @Override
    public ResponseEntity<String> auth(EmployeeDTO attempt, HttpServletRequest request, HttpServletResponse response, boolean rememberMe) throws IOException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {


        EmployeeDTO logIn = employeeDAO.validate(attempt.getUsername());

        UserDetails userDetails = employeeDetailService.loadUserByUsername(attempt.getUsername());

        boolean isLogIn = false;

        if(userDetails != null) {
            isLogIn = passwordEncoder.matches(attempt.getPassword(), userDetails.getPassword());
        }

        Map<String, Object> data = new HashMap<>();


        if (isLogIn) {
            HttpSession session = request.getSession();
            logIn.setPassword("");
            session.setAttribute("logIn", logIn);
            // 사용자 정보를 이용하여 인증 객체 생성
            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
            // 인증 객체를 현재 스레드의 SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(auth);

            if (rememberMe) {
                UUID seriesUuid = UUID.randomUUID();
                String series = seriesUuid.toString();

                // 임의의 시크릿 키 생성 (16 바이트 길이의 바이트 배열)
                byte[] secretKeyBytes = new byte[16];
                // secretKeyBytes에 무작위로 바이트 할당하는 로직
                SecretKey secretKey = new SecretKeySpec(secretKeyBytes, "AES");
                // 암호화를 위한 Cipher 객체 생성
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                // 암호화할 값을 바이트 배열로 변환
                byte[] valueBytes = series.getBytes();
                // 암호화 수행
                byte[] encryptedBytes = cipher.doFinal(valueBytes);
                // Base64로 인코딩하여 문자열로 변환
                String tokenValue = Base64.getEncoder().encodeToString(encryptedBytes);

                PersistentRememberMeToken token = new PersistentRememberMeToken(attempt.getUsername(), series, tokenValue, new Date());
                persistentTokenRepository.createNewToken(token);

                Cookie rememberMeCookie = new Cookie("remember-me", token.getUsername() + ":" + token.getSeries() + ":" + token.getTokenValue());
                rememberMeCookie.setMaxAge(86400); // 쿠키의 유효 기간 설정 (예: 1일)
                rememberMeCookie.setPath("/autoLogin"); // 쿠키의 유효 경로 설정
                response.addCookie(rememberMeCookie);
            }

            data.put("logIn", "success");
            data.put("role", logIn.getRole());
            data.put("name", logIn.getName());
        } else {
            SecurityContextHolder.clearContext();
            data.put("logIn", "failed");
        }


        // JSON 문자열 생성
        String json = objectMapper.writeValueAsString(data);

        // HTTP 응답 생성
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    @Override
    public ResponseEntity<String> register(EmployeeDTO attempt) throws IOException {
        attempt.setPassword(passwordEncoder.encode(attempt.getPassword()));

        Map<String, Object> data = new HashMap<>();

        if(validate(attempt.getUsername())){
            employeeDAO.register(attempt);
            data.put("register","success");
        }else{
            data.put("register","fail");
        }

        // JSON 문자열 생성
        String json = objectMapper.writeValueAsString(data);

        // HTTP 응답 생성
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    @Override
    public ResponseEntity<String> getLogInInfo(HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        EmployeeDTO employeeDTO = (EmployeeDTO) session.getAttribute("logIn");
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

    @Override
    public ResponseEntity<String> updateInfo(EmployeeDTO employeeDTO, String dept, HttpServletRequest request) throws IOException{
        Map<String, Object> data = new HashMap<>();

        EmployeeDTO selectOne = employeeDAO.selectOne(employeeDTO.getId());

        if(!employeeDTO.getPassword().equals("")) {
            selectOne.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        }
        selectOne.setName(employeeDTO.getName());
        selectOne.setDeptId(deptDAO.selectByName(dept).getId());
        selectOne.setPhoneNumber(employeeDTO.getPhoneNumber());
        selectOne.setDetail(employeeDTO.getDetail());

        HttpSession session = request.getSession();

        session.setAttribute("logIn",selectOne);

        employeeDAO.update(selectOne);

        data.put("update","success");

        // JSON 문자열 생성
        String json = objectMapper.writeValueAsString(data);

        // HTTP 응답 생성
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }
}

