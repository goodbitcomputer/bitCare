package com.bit.bitcare.dao;

import com.bit.bitcare.model.HistoryDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface HistoryDAO {
    public List<HistoryDTO> selectByPatientId(int patientId);
    public HistoryDTO selectOne(int id);
    public List<HistoryDTO> selectAll();
    public void delete(int id);
    public void insert(HistoryDTO historyDTO);
}
