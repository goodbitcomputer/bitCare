package com.bit.bitcare.service;

import com.bit.bitcare.model.*;
import com.bit.bitcare.vo.doctor.UpsertVO;
import com.google.gson.JsonObject;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface NurseService {
    public List<WaitingDTO> getWaitingData(HttpServletRequest request);
    public List<WaitingDTO> getWaitingCmopletedData(HttpServletRequest request);
    public List<HistoryDTO> getHistoryList(int patientId);
    public JsonObject getHistoryAddData(int historyId);
    public List<VisitDTO> getVisitByType(String type);
    public ReceiptDTO getReceiptData(Map<String, Object> requestData);
    public Boolean payment(Map<String, Object> requestData);
    public String canclePayment(Map<String, Object> requestData);
    public ResponseEntity<String> selectOne(int patientId) throws IOException;
}
