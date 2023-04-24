package com.bit.bitcare.dao;

import com.bit.bitcare.model.MessageDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MessageDAO {
    public List<MessageDTO> selectByReceiver(String logInName);
    public List<MessageDTO> selectBySender(String logInName);
    public MessageDTO selectOne(int id);
    public void insert(MessageDTO messageDTO);
    public void update(MessageDTO messageDTO);
    public void delete(int id);
}
