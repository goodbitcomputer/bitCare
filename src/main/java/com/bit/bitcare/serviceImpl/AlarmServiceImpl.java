package com.bit.bitcare.serviceImpl;

import com.bit.bitcare.dao.AlarmDAO;
import com.bit.bitcare.dao.EmployeeDAO;
import com.bit.bitcare.dao.MessageDAO;
import com.bit.bitcare.model.AlarmDTO;
import com.bit.bitcare.model.EmployeeDTO;
import com.bit.bitcare.model.MessageDTO;
import com.bit.bitcare.service.AlarmService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ---------------------------------------------------------------------------
 * 2023.05.16
 *
 * AlarmServiceImpl
 * AlarmService 인터페이스에 대한 구현체
 * SimpleMessagingTemplate, AlarmDAO, EmployeeDAO, MessageDAO, ObjectMapper 객체 사용
 * 메소드 목록
 * 1. SocketHandler : 클라이언트로 전달받은 메시지를 MessageDTO, AlarmDTO 객체로 변환 후 receiver 가 구독한 주소로 전송하는 메소드
 * 2. getReceiveList : 클라이언트 접속자의 받은 알림을 AlarmDAO 를 통해 List 객체로 변환 후 리턴하는 메소드
 * 3. deleteAlarm : Alarm 의 id를 전달받아 삭제시키는 메소드
 * ---------------------------------------------------------------------------
 * 2023.05.22
 *
 * 메소드 목록 추가
 * 4. allDeleteAlarm : 로그인한 사용자의 name 을 전달받아 전체 알람을 삭제시키는 메소드
 * 5. getReceiveMessageList : 클라이언트 접속자의 받은 메세지 리스트를 MessageDAO 를 통해 List 객체로 변환 후 리턴하는 메소드
 * 6. deleteMessage : Message 의 id 를 전달 받아 삭제시키는 메소드
 * 7. allDeleteMessage : 로그인한 사용자의 name 을 전달받아 전체 메세지를 삭제시키는 메소드
 * 8. getSendMessageList : 클라이언트 접속자의 발송 메세지 리스트를 MessageDAO 를 통해 List 객체로 변환 후 리턴하는 메소드
 * ---------------------------------------------------------------------------
 * 2023.05.24
 *
 * 메소드 목록 변경
 * deleteMessage -> deleteReceiveMessage : 받은 메시지 삭제
 * allDeleteMessage -> allDeleteReceiveMessage : 받은 모든 메시지 삭제
 */
@Service
public class AlarmServiceImpl implements AlarmService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final AlarmDAO alarmDAO;
    private final EmployeeDAO employeeDAO;
    private final MessageDAO messageDAO;
    private final ObjectMapper objectMapper;

    public AlarmServiceImpl(SimpMessagingTemplate simpMessagingTemplate, AlarmDAO alarmDAO, EmployeeDAO employeeDAO, MessageDAO messageDAO, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.alarmDAO = alarmDAO;
        this.employeeDAO = employeeDAO;
        this.messageDAO = messageDAO;
    }

    @Override
    public void SocketHandler(String receive, MessageDTO messageDTO, AlarmDTO alarmDTO, String payload) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(payload);
        String connectType = jsonNode.get("connectType").asText();

        int id = messageDTO.getId();
        // vo에서 getter로 userName을 가져옵니다.
        MessageDTO selectMessage = messageDAO.selectOne(id);

        if(connectType.equals("send")) {
            String sender = messageDTO.getSender();
            String receiver = messageDTO.getReceiver();
            String content = messageDTO.getContent();
            String messageFile = messageDTO.getMessageFile();
            String state = messageDTO.getReceiveState();
            String type;
            if (messageDTO.getReceiver() != null) {
                type = "message";
            } else {
                type = "announcement";
            }

            messageDAO.insert(messageDTO);

            // 생성자로 반환값을 생성합니다.
            AlarmDTO alarm = new AlarmDTO(id, sender, receiver, content, type, alarmDTO.getEntryDate(), state);
            alarmDAO.insert(alarm);
            // 반환
            simpMessagingTemplate.convertAndSend("/send/" + receiver, alarm);
        } else if(connectType.equals("cancel")){
            messageDAO.delete(id);
            simpMessagingTemplate.convertAndSend("/send/" + selectMessage.getReceiver(), "cancel");
        } else if(connectType.equals("read")){
            simpMessagingTemplate.convertAndSend("/send/" + selectMessage.getSender(), "read");
        }
    }

    @Override
    public ResponseEntity<String> getReceiveList(HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        EmployeeDTO employeeDTO = (EmployeeDTO) session.getAttribute("logIn");
        boolean isLoggedIn = (employeeDTO != null);

        // JSON 데이터 생성
        Map<String, Object> data = new HashMap<>();
        if (isLoggedIn) {
            List<AlarmDTO> receiveList = alarmDAO.selectByReceiver(employeeDTO.getName());
            data.put("isLoggedIn", isLoggedIn);
            if (receiveList.isEmpty()) {
                data.put("receiveList", null);
            } else {
                data.put("receiveList", receiveList);
            }
        }

        // JSON 문자열 생성
        String json = objectMapper.writeValueAsString(data);

        // HTTP 응답 생성
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    @Override
    public ResponseEntity<String> getReceiveMessageList(HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        EmployeeDTO employeeDTO = (EmployeeDTO) session.getAttribute("logIn");
        boolean isLoggedIn = (employeeDTO != null);

        // JSON 데이터 생성
        Map<String, Object> data = new HashMap<>();
        if (isLoggedIn) {
            List<MessageDTO> receiveList = messageDAO.selectByReceiver(employeeDTO.getName());
            data.put("isLoggedIn", isLoggedIn);
            if (receiveList.isEmpty()) {
                data.put("receiveList", null);
            } else {
                data.put("receiveList", receiveList);
            }
        }

        // JSON 문자열 생성
        String json = objectMapper.writeValueAsString(data);

        // HTTP 응답 생성
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    @Override
    public ResponseEntity<String> getReceiveMessage(int messageId) throws IOException {

        // JSON 데이터 생성
        Map<String, Object> data = new HashMap<>();

        MessageDTO receiveMessage = messageDAO.selectOne(messageId);
        if (receiveMessage == null) {
            data.put("receiveMessage", null);
        } else {
            receiveMessage.setReceiveState("read");
            messageDAO.update(receiveMessage);
            data.put("receiveMessage", receiveMessage);
        }

        // JSON 문자열 생성
        String json = objectMapper.writeValueAsString(data);

        // HTTP 응답 생성
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    @Override
    public ResponseEntity<String> getSendMessageList(HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        EmployeeDTO employeeDTO = (EmployeeDTO) session.getAttribute("logIn");
        boolean isLoggedIn = (employeeDTO != null);

        // JSON 데이터 생성
        Map<String, Object> data = new HashMap<>();
        if (isLoggedIn) {
            List<MessageDTO> sendList = messageDAO.selectBySender(employeeDTO.getName());
            data.put("isLoggedIn", isLoggedIn);
            if (sendList.isEmpty()) {
                data.put("sendList", null);
            } else {
                data.put("sendList", sendList);
            }
        }

        // JSON 문자열 생성
        String json = objectMapper.writeValueAsString(data);

        // HTTP 응답 생성
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    @Override
    public void deleteAlarm(int alarmId) {
        alarmDAO.delete(alarmId);
    }

    @Override
    public void allDeleteAlarm(String name) {
        alarmDAO.allDelete(name);
    }

    @Override
    public void deleteMessage(int messageId) {
        messageDAO.delete(messageId);
    }

    @Override
    public void deleteReceiveMessage(int alarmId) {
        messageDAO.deleteReceive(alarmId);
    }

    @Override
    public void allDeleteReceiveMessage(String name) {
        messageDAO.allDeleteReceive(name);
    }
}
