package com.bit.bitcare.dao;

import com.bit.bitcare.model.HistoryImageDTO;
import com.bit.bitcare.model.ReceiptDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface HistoryImageDAO {
    public HistoryImageDTO selectById(int id);
    public List<HistoryImageDTO> selectByHistoryId(int historyId);
    public void insert(HistoryImageDTO historyImageDTO);
    public void update(HistoryImageDTO historyImageDTO);
}
