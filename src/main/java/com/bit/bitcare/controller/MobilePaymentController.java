package com.bit.bitcare.controller;

import com.bit.bitcare.awsS3.AwsS3;
import com.bit.bitcare.awsS3.AwsS3Service;
import com.bit.bitcare.model.BodyCategoryDTO;
import com.bit.bitcare.model.HistoryDTO;
import com.bit.bitcare.model.ReceiptDTO;
import com.bit.bitcare.service.MobileDoctorService;
import com.bit.bitcare.service.MobilePaymentService;
import com.bit.bitcare.service.MobileService;
import com.bit.bitcare.serviceImpl.MobileServiceImpl;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mobile/payment")
public class MobilePaymentController {
    private MobilePaymentService mobilePaymentService;
    @Autowired

    public MobilePaymentController(MobilePaymentService mobilePaymentService) {
        this.mobilePaymentService = mobilePaymentService;
    }

    @ResponseBody
    @PostMapping("/getReceiptList_proc")
    public List<ReceiptDTO> getReceiptData(@RequestBody Map<String, Object> requestData, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return mobilePaymentService.getReciptListByPatientId(requestData);
    }

    @ResponseBody
    @PostMapping("/payment_proc")
    public IamportResponse<Payment> payment(@RequestBody Map<String, Object> requestData, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IamportResponseException {
        System.out.println("payment_proc");
        return mobilePaymentService.setPaymentData(requestData);
    }




}
