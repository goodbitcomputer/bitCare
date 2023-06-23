package com.bit.bitcare.controller;

import com.bit.bitcare.model.*;
import com.bit.bitcare.service.DoctorService;
import com.bit.bitcare.service.NurseService;
import com.bit.bitcare.serviceImpl.DoctorServiceImpl;
import com.bit.bitcare.serviceImpl.NurseServiceImpl;
import com.bit.bitcare.vo.doctor.UpsertVO;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/nurse")
public class NurseController {
    private NurseService nurseService;

    public NurseController(NurseServiceImpl nurseService) {
        this.nurseService = nurseService;
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
        return nurseService.getWaitingData(request);
    }
    // 진료완료 버튼 로직
    @ResponseBody
    @GetMapping("/getWaitingCmopletedData")
    public List<WaitingDTO> getWaitingCmopletedData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return nurseService.getWaitingCmopletedData(request);
    }
    @ResponseBody
    @PostMapping("/getHistoryList")
    public List<HistoryDTO> getHistoryList(@RequestBody Map<String, Object> requestData, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int patientId = Integer.parseInt(requestData.get("patientId").toString());
        return nurseService.getHistoryList(patientId);
    }
    @ResponseBody
    @PostMapping("/getHistoryAddData")
    public String getHistoryAddData(@RequestBody Map<String, Object> requestData, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int historyId = Integer.parseInt(requestData.get("historyId").toString());
        return nurseService.getHistoryAddData(historyId).toString();
    }
    @ResponseBody
    @PostMapping("/getVisitData")
    public List<VisitDTO> getVisitData(@RequestBody Map<String, Object> requestData, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String visitType = requestData.get("visitType").toString();
        return nurseService.getVisitByType(visitType);
    }
    @ResponseBody
    @PostMapping("/getReceiptData")
    public ReceiptDTO getReceiptData(@RequestBody Map<String, Object> requestData, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return nurseService.getReceiptData(requestData);
    }

    @ResponseBody
    @PostMapping("/receipt_proc")
    public boolean payment(@RequestBody Map<String, Object> requestData, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IamportResponseException {
        return nurseService.payment(requestData);
    }
    @ResponseBody
    @PostMapping("/cancle_receipt_proc")
    public String canclePayment(@RequestBody Map<String, Object> requestData, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IamportResponseException {
        return nurseService.canclePayment(requestData);
    }

    @ResponseBody
    @GetMapping("/select_one")
    public ResponseEntity<String> selectOne(@RequestParam int patientId) throws IOException{
        return nurseService.selectOne(patientId);
    }
}
