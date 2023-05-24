package com.bit.bitcare.service;

import com.bit.bitcare.model.DiagnoseDTO;
import com.bit.bitcare.model.DiseaseDTO;
import com.bit.bitcare.model.EmployeeDTO;
import com.bit.bitcare.model.WaitingDTO;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface DoctorService {
    public JsonObject sbFilterSearch(String filterMessage);
    public List<DiagnoseDTO> cbFilterSearch(String filterMessage);
    public List<WaitingDTO> getWaitingData(HttpServletRequest request);
}
