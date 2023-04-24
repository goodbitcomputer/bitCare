package com.bit.bitcare.model;

import lombok.Data;

@Data
public class ReceiptDTO {
    int historyId;
    String name;
    int payment;
    int visitId;
}
