package com.bit.bitcare.service;

import com.bit.bitcare.model.*;
import com.bit.bitcare.vo.doctor.UpsertVO;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface DoctorService {
    public JsonObject sbFilterSearch(String filterMessage);
    public List<DiagnoseDTO> cbFilterSearch(String filterMessage);
    public List<WaitingDTO> getWaitingData(HttpServletRequest request);
    public List<WaitingDTO> getWaitingCmopletedData(HttpServletRequest request);
    public JsonObject historyUpsert(UpsertVO requestData, HttpServletRequest request);
    public List<HistoryDTO> getHistoryList(int patientId);
    public JsonObject getHistoryAddData(int historyId);
    public void completedWaiting(WaitingDTO waitingDTO);
}
