package com.bit.bitcare.service;

import com.bit.bitcare.model.AlarmDTO;
import com.bit.bitcare.model.MessageDTO;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface AlarmService {
    public void SocketHandler(String send, MessageDTO messageDTO, AlarmDTO alarmDTO);
    public ResponseEntity<String> getReceiveList(HttpServletRequest request) throws IOException;
    public void deleteMessage(int messageId);
}
