package com.bit.bitcare.dao;

import com.bit.bitcare.model.EmployeeDTO;
import com.bit.bitcare.model.ReceiptDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ReceiptDAO {
    public ReceiptDTO selectOne(int id);
    public ReceiptDTO selectByHistoryId(int historyId);
    public List<ReceiptDTO> selectByPatientId(int patientId);
    public void insert(ReceiptDTO receiptDTO);
    public void update(ReceiptDTO receiptDTO);
    public void delete(int id);
}
