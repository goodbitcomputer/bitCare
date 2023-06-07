package com.bit.bitcare.controller;

import com.bit.bitcare.model.DeptDTO;
import com.bit.bitcare.model.DiagnoseDTO;
import com.bit.bitcare.model.PatientDTO;
import com.bit.bitcare.service.DoctorService;
import com.bit.bitcare.service.MobileService;
import com.bit.bitcare.serviceImpl.DoctorServiceImpl;
import com.bit.bitcare.serviceImpl.MobileServiceImpl;
import com.google.gson.JsonObject;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
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
    private IamportClient api;

    public MobileController(MobileServiceImpl mobileService) {
        this.mobileService = mobileService;
        // REST API 키와 REST API secret 를 아래처럼 순서대로 입력한다.
        this.api = new IamportClient("6133142457574138","u39hsQc6oAvLnCWrgpTmWHsotpFlcrvr7gVQemdynSWSXAM797D9HLDW4IknsXRDakIRg6bujule91E8");
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
    @PostMapping("/register_proc")
    public Boolean register(@RequestBody Map<String, Object> requestData, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        return mobileService.register(requestData);
    }

    @ResponseBody
    @PostMapping("/payment_proc")
    public IamportResponse<Payment> payment(@RequestBody Map<String, Object> requestData, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IamportResponseException {
        String impUid = (String)requestData.get("impUid");
        int patientId = (int) requestData.get("patientId");

        System.out.println("impUid: "+ impUid);
        System.out.println("patientId: "+ patientId);
        IamportResponse<Payment> payment = api.paymentByImpUid(impUid);

        System.out.println(payment.getResponse());
        System.out.println(payment.getResponse().getAmount());

        return api.paymentByImpUid(impUid);
    }



}
