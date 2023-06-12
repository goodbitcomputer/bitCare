package com.bit.bitcare.serviceImpl;

import com.bit.bitcare.dao.*;
import com.bit.bitcare.model.*;
import com.bit.bitcare.service.MobilePaymentService;
import com.bit.bitcare.service.MobileService;
import com.google.gson.JsonObject;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class MobilePaymentServiceImpl implements MobilePaymentService {


    private IamportClient api;
    private final ReceiptDAO receiptDAO;


    public MobilePaymentServiceImpl(ReceiptDAO receiptDAO) {
        // REST API 키와 REST API secret 를 아래처럼 순서대로 입력한다.
        this.api = new IamportClient("6133142457574138", "u39hsQc6oAvLnCWrgpTmWHsotpFlcrvr7gVQemdynSWSXAM797D9HLDW4IknsXRDakIRg6bujule91E8");
        this.receiptDAO = receiptDAO;
    }


    @Override
    public List<ReceiptDTO> getReciptListByPatientId(Map<String, Object> requestData) {
        int patientId = (int) requestData.get("patientId");
        List<ReceiptDTO> list = receiptDAO.selectByPatientId(patientId);
        return list;
    }

    @Override
    public IamportResponse<Payment> setPaymentData(Map<String, Object> requestData) throws IamportResponseException, IOException {
        try {
            String impUid = requestData.get("impUid").toString();
//            int patientId = (int) requestData.get("patientId");
            int historyId = (int) requestData.get("historyId");


            IamportResponse<Payment> payment = api.paymentByImpUid(impUid);
//            Payment paymentData = payment.getResponse();

            ReceiptDTO receiptDTO = receiptDAO.selectByHistoryId(historyId);
            receiptDTO.setImpUid(impUid);
            receiptDTO.setPaidAt(payment.getResponse().getPaidAt());
            receiptDTO.setCardName(payment.getResponse().getCardName());
            receiptDTO.setCardNumber(payment.getResponse().getCardNumber());
            receiptDTO.setApplyNum(payment.getResponse().getApplyNum());

            System.out.println(receiptDTO);
            receiptDAO.update(receiptDTO);

            return payment;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}

