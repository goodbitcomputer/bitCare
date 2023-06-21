package com.bit.bitcare.service;

import com.bit.bitcare.model.PatientDTO;
import com.bit.bitcare.model.ReceiptDTO;
import com.bit.bitcare.model.SearchDTO;
import com.google.gson.JsonObject;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SearchService {
    public JsonObject getPatientData(SearchDTO searchDTO);
}
