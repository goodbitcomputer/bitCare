package com.bit.bitcare.serviceImpl;

import com.bit.bitcare.dao.PatientDAO;
import com.bit.bitcare.dao.ReceiptDAO;
import com.bit.bitcare.model.*;
import com.bit.bitcare.service.MobilePaymentService;
import com.bit.bitcare.service.SearchService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {
    private final PatientDAO patientDAO;


    public SearchServiceImpl(PatientDAO patientDAO) {
        this.patientDAO = patientDAO;
    }

    @Override
    public JsonObject getPatientData(SearchDTO searchDTO) {
        JsonObject result = new JsonObject();

        List<PatientDTO> patientList = patientDAO.searchAll(searchDTO);
        int totalRecordCount = patientDAO.count(searchDTO);
        PaginationDTO paginationDTO = new PaginationDTO(totalRecordCount, searchDTO);

        System.out.println("patientList: "+ patientList);
        System.out.println("totalRecordCount: "+ totalRecordCount);
        System.out.println("paginationDTO: "+ paginationDTO);


        // patientList 담기
        JsonArray patientArray = new JsonArray();
        for (PatientDTO p : patientList) {
            JsonObject object = new JsonObject();
            object.addProperty("id", p.getId());
            object.addProperty("phoneNumber", p.getPhoneNumber());
            object.addProperty("identityNumber", p.getIdentityNumber());
            object.addProperty("birth", p.getBirth().toString());
            object.addProperty("gender", p.getGender());
            object.addProperty("name", p.getName());
            object.addProperty("addressId", p.getAddressId());
            object.addProperty("symptomDetail", p.getSymptomDetail());

            patientArray.add(object);
        }

        // paginationDTO 담기
        JsonObject paginationObject = new JsonObject();
        paginationObject.addProperty("totalRecordCount", paginationDTO.getTotalRecordCount());
        paginationObject.addProperty("totalPageCount", paginationDTO.getTotalPageCount());
        paginationObject.addProperty("startPage", paginationDTO.getStartPage());
        paginationObject.addProperty("endPage", paginationDTO.getEndPage());
        paginationObject.addProperty("current", paginationDTO.getCurrent());
        paginationObject.addProperty("limitStart", paginationDTO.getLimitStart());
        paginationObject.addProperty("existPrevPage", paginationDTO.isExistPrevPage());
        paginationObject.addProperty("existNextPage", paginationDTO.isExistNextPage());

        result.addProperty("patientList", patientArray.toString());
        result.addProperty("paginationDTO", paginationObject.toString());


        return result;
    }
}

