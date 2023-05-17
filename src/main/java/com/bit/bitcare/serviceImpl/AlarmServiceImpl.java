package com.bit.bitcare.serviceImpl;

import com.bit.bitcare.dao.AlarmDAO;
import com.bit.bitcare.dao.EmployeeDAO;
import com.bit.bitcare.dao.MessageDAO;
import com.bit.bitcare.model.AlarmDTO;
import com.bit.bitcare.model.EmployeeDTO;
import com.bit.bitcare.model.MessageDTO;
import com.bit.bitcare.service.AlarmService;
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
 * 3. deleteMessage : Message 의 id를 전달받아 삭제시키는 메소드
 */
@Service
public class AlarmServiceImpl implements AlarmService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final AlarmDAO alarmDAO;
    private final EmployeeDAO employeeDAO;
    private final MessageDAO messageDAO;
    private final ObjectMapper objectMapper;
    public AlarmServiceImpl(SimpMessagingTemplate simpMessagingTemplate, AlarmDAO alarmDAO, EmployeeDAO employeeDAO, MessageDAO messageDAO, ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.alarmDAO = alarmDAO;
        this.employeeDAO = employeeDAO;
        this.messageDAO = messageDAO;
    }
    @Override
    public void SocketHandler(String receive, MessageDTO messageDTO, AlarmDTO alarmDTO){
        int id = alarmDTO.getId();
        // vo에서 getter로 userName을 가져옵니다.
        String sender = messageDTO.getSender();
        String receiver = messageDTO.getReceiver();
        String content = messageDTO.getContent();
        String messageFile = messageDTO.getMessageFile();
        String state = messageDTO.getState();
        String type;
        if(messageDTO.getReceiver() != null){
            type = "message";
        } else{
            type = "announcement";
        }

        messageDAO.insert(messageDTO);

        // 생성자로 반환값을 생성합니다.
        AlarmDTO alarm = new AlarmDTO(id, sender, receiver, content, type, alarmDTO.getEntryDate(), state);
        alarmDAO.insert(alarm);
        // 반환
        simpMessagingTemplate.convertAndSend("/send/"+receiver, alarm);
    }

    @Override
    public ResponseEntity<String> getReceiveList(HttpServletRequest request) throws IOException{
        HttpSession session = request.getSession();
        EmployeeDTO employeeDTO = (EmployeeDTO) session.getAttribute("logIn");
        System.out.println(employeeDTO);
        boolean isLoggedIn = (employeeDTO != null);

        // JSON 데이터 생성
        Map<String, Object> data = new HashMap<>();
        if(isLoggedIn) {
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
    public void deleteMessage(int messageId){
        alarmDAO.delete(messageId);
    }
}
