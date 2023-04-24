package com.bit.bitcare.dao;

import com.bit.bitcare.model.HistoryImageDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface HistoryImageDAO {
    public List<HistoryImageDTO> selectByHistoryId(int historyId);
    public void insert(HistoryImageDTO historyImageDTO);
}
