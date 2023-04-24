package com.bit.bitcare.dao;

import com.bit.bitcare.model.ReceiptDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ReceiptDAO {
    public List<ReceiptDTO> selectByHistoryId(int historyId);
    public void insert(ReceiptDTO receiptDTO);
}
