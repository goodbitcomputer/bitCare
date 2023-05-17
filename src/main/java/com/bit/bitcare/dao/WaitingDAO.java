package com.bit.bitcare.dao;

import com.bit.bitcare.model.WaitingDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface WaitingDAO {
    public WaitingDTO selectOne(int id);
    public List<WaitingDTO> selectAll();
    public List<WaitingDTO> selectAllByDept(int deptId);
    public void delete(int id);
    public void insert(WaitingDTO waitingDTO);
}
