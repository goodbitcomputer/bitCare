package com.bit.bitcare.dao;

import com.bit.bitcare.model.HistoryDiseaseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface HistoryDiseaseDAO {
    public List<HistoryDiseaseDTO> selectByHistoryId(int historyId);
    public void insert(HistoryDiseaseDTO historyDiseaseDTO);
}
