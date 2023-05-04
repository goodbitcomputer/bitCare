package com.bit.bitcare.controller;

import com.bit.bitcare.model.DeptDTO;
import com.bit.bitcare.model.DiagnoseDTO;
import com.bit.bitcare.model.PatientDTO;
import com.bit.bitcare.service.DoctorService;
import com.bit.bitcare.service.MobileService;
import com.bit.bitcare.serviceImpl.DoctorServiceImpl;
import com.bit.bitcare.serviceImpl.MobileServiceImpl;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mobile")
public class MobileController {
    private MobileService mobileService;

    public MobileController(MobileServiceImpl mobileService) {
        this.mobileService = mobileService;
    }

    @ResponseBody
    @GetMapping("/getDeptData")
    public List<DeptDTO> getDeptData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return mobileService.getDeptAll();
    }

    @ResponseBody
    @PostMapping("/auth")
    public PatientDTO auth(@RequestBody Map<String, Object> requestData, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        return mobileService.auth(requestData);
    }

    @ResponseBody
    @PostMapping("/insertPatient")
    public String insertPatient(@RequestBody Map<String, Object> requestData, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        return mobileService.insertPatient(requestData).toString();
    }

    @ResponseBody
    @PostMapping("/register")
    public Boolean register(@RequestBody Map<String, Object> requestData, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        return mobileService.register(requestData);
    }


}
