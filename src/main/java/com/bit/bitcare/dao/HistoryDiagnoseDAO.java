package com.bit.bitcare.dao;

import com.bit.bitcare.model.HistoryDiagnoseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface HistoryDiagnoseDAO {
    public List<HistoryDiagnoseDTO> selectByHistoryId(int historyId);
    public void insert(HistoryDiagnoseDTO historyDiagnoseDTO);
}
