package com.bit.bitcare.service;

import com.bit.bitcare.model.AlarmDTO;
import com.bit.bitcare.model.MessageDTO;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface AlarmService {
    public void SocketHandler(String send, MessageDTO messageDTO, AlarmDTO alarmDTO);
    public ResponseEntity<String> getReceiveList(HttpServletRequest request) throws IOException;
    public ResponseEntity<String> getReceiveMessageList(HttpServletRequest request) throws IOException;
    public ResponseEntity<String> getReceiveMessage(int messageId) throws IOException;
    public ResponseEntity<String> getSendMessageList(HttpServletRequest request) throws IOException;
    public void deleteAlarm(int messageId);
    public void allDeleteAlarm(String name);
    public void deleteMessage(int messageId);
    public void allDeleteMessage(String name);
}
