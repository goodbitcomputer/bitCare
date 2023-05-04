package com.bit.bitcare.service;

import com.bit.bitcare.model.DeptDTO;
import com.bit.bitcare.model.DiagnoseDTO;
import com.bit.bitcare.model.PatientDTO;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

public interface MobileService {
    public List<DeptDTO> getDeptAll();
    public PatientDTO auth(Map<String, Object> requestData);
    public JsonObject insertPatient(Map<String, Object> requestData);
    public Boolean register(Map<String, Object> requestData);
}
