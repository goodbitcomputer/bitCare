package com.bit.bitcare.service;

import com.bit.bitcare.model.DiseaseDTO;
import com.bit.bitcare.model.EmployeeDTO;
import com.google.gson.JsonObject;

import java.util.List;

public interface DoctorService {
    public JsonObject filterSearch(String filterMessage);
}
