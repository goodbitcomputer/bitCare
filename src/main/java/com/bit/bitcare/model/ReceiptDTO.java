package com.bit.bitcare.model;

import lombok.Data;

import java.util.Date;

@Data
public class ReceiptDTO {
    int id;
    int historyId;
    int patientId;
    String content;
    int payment;
    String impUid;
    Date paidAt;
    String cardName;
    String cardNumber;
    String applyNum;

    String memo;
    String symptomDetail;
    Date entryDate;
    String visit;
    String name;
    String dept;

}
