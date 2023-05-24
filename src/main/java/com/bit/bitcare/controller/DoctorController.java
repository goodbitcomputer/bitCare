package com.bit.bitcare.controller;

import com.bit.bitcare.lucene.DiseaseIndexer;
import com.bit.bitcare.model.*;
import com.bit.bitcare.service.DoctorService;
import com.bit.bitcare.serviceImpl.DoctorServiceImpl;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

    /**
     * ---------------------------------------------------------------------------
     * 2023.04.26 홍사범
     * <p>
     * 상병테이블 검색기능 구현
     * 필터메세지로 관련 상병 데이터 검색
     * ---------------------------------------------------------------------------
     */
    @ResponseBody
    @RequestMapping(value = "/sbModalFilter", method = RequestMethod.POST)
    public String sbModalFilter(@RequestBody Map<String, Object> requestData, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String message = (String) requestData.get("filterMessage");
        return doctorService.sbFilterSearch(message).toString();
    }

    /**
     * ---------------------------------------------------------------------------
     * 2023.04.27 홍사범
     * <p>
     * 처방테이블 검색기능 구현
     * 필터메세지로 관련 처방 데이터 검색
     * ---------------------------------------------------------------------------
     */
    @ResponseBody
    @RequestMapping(value = "/cbModalFilter", method = RequestMethod.POST)
    public List<DiagnoseDTO> cbModalFilter(@RequestBody Map<String, Object> requestData, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String message = (String) requestData.get("filterMessage");
        return doctorService.cbFilterSearch(message);
    }

    /**
     * ---------------------------------------------------------------------------
     * 2023.05.24 홍사범
     * <p>
     * doctorPage waiting service
     * 의사 부서에 맞게 waiting data 가져오기
     * ---------------------------------------------------------------------------
     */
    @ResponseBody
    @GetMapping("/getWaitingData")
    public List<WaitingDTO> getWaitingData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return doctorService.getWaitingData(request);
    }

}
