package com.bit.bitcare.model;

import lombok.Data;

import java.util.Date;

@Data
public class WaitingDTO {
    int patientId;
    int deptId;
    int typeId;
    String symptom;
    Date entryDate;
    Date reservationDate;
}
