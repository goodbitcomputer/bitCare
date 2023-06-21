package com.bit.bitcare.controller;

import com.bit.bitcare.model.*;
import com.bit.bitcare.service.DoctorService;
import com.bit.bitcare.service.MobileDoctorService;
import com.bit.bitcare.service.MobilePaymentService;
import com.bit.bitcare.service.SearchService;
import com.google.gson.JsonObject;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/search")
public class SearchController {
    private SearchService searchService;
    private DoctorService doctorService;
    private MobileDoctorService mobileDoctorService;
    @Autowired

    public SearchController(MobileDoctorService mobileDoctorService, DoctorService doctorService, SearchService searchService) {
        this.searchService = searchService;
        this.doctorService = doctorService;
        this.mobileDoctorService = mobileDoctorService;
    }

    @ResponseBody
    @RequestMapping(value = "/search_proc", method = RequestMethod.POST)
    public String search_proc(@RequestBody SearchDTO searchDTO, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return searchService.getPatientData(searchDTO).toString();
    }

    @ResponseBody
    @PostMapping("/getHistoryList")
    public List<HistoryDTO> getHistoryList(@RequestBody Map<String, Object> requestData, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int patientId = Integer.parseInt(requestData.get("patientId").toString());
        return doctorService.getHistoryList(patientId);
    }
    @ResponseBody
    @PostMapping("/getHistoryAddData")
    public String getHistoryAddData(@RequestBody Map<String, Object> requestData, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int historyId = Integer.parseInt(requestData.get("historyId").toString());
        return doctorService.getHistoryAddData(historyId).toString();
    }

    @ResponseBody
    @GetMapping("/getBodyCategoryData")
    public List<BodyCategoryDTO> getBodyCategoryData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return mobileDoctorService.getBodyCategoryAll();
    }





}
