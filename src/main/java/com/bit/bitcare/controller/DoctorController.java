package com.bit.bitcare.controller;

import com.bit.bitcare.lucene.DiseaseIndexer;
import com.bit.bitcare.model.DiagnoseDTO;
import com.bit.bitcare.model.DiseaseDTO;
import com.bit.bitcare.service.DoctorService;
import com.bit.bitcare.serviceImpl.DoctorServiceImpl;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/doctor")
public class DoctorController {
    private DoctorService doctorService;

    public DoctorController(DoctorServiceImpl doctorService) {
        this.doctorService = doctorService;
    }

    @ResponseBody
    @RequestMapping(value = "/sbModalFilter", method = RequestMethod.POST)
    public String sbModalFilter(@RequestBody Map<String, Object> requestData, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String message = (String) requestData.get("filterMessage");
        return doctorService.sbFilterSearch(message).toString();
    }

    @ResponseBody
    @RequestMapping(value = "/cbModalFilter", method = RequestMethod.POST)
    public List<DiagnoseDTO> cbModalFilter(@RequestBody Map<String, Object> requestData, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String message = (String) requestData.get("filterMessage");
        return doctorService.cbFilterSearch(message);
    }
}
