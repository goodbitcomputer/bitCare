package com.bit.bitcare.controller;

import com.bit.bitcare.model.PatientDTO;
import com.bit.bitcare.model.ReceiptDTO;
import com.bit.bitcare.model.SearchDTO;
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
    @Autowired

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @ResponseBody
    @RequestMapping(value = "/search_proc", method = RequestMethod.POST)
    public String search_proc(@RequestBody SearchDTO searchDTO, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return searchService.getPatientData(searchDTO).toString();
    }





}
