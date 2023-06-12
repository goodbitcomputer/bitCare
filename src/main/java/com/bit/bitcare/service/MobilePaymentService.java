package com.bit.bitcare.service;

import com.bit.bitcare.awsS3.AwsS3;
import com.bit.bitcare.model.*;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MobilePaymentService {
    public List<ReceiptDTO> getReciptListByPatientId(Map<String, Object> requestData);
    public IamportResponse<Payment> setPaymentData(Map<String, Object> requestData) throws IamportResponseException, IOException;
}
