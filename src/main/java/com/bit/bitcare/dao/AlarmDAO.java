package com.bit.bitcare.dao;

import com.bit.bitcare.model.AlarmDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AlarmDAO {
    public List<AlarmDTO> selectByReceiver(String logInName);
    public AlarmDTO selectOne(int id);
    public void insert(AlarmDTO alarmDTO);
    public void update(AlarmDTO alarmDTO);
    public void delete(int id);
    public void allDelete(String name);
}
