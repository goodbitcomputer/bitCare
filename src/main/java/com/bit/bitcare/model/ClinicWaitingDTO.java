package com.bit.bitcare.model;

import lombok.Data;

import java.util.Date;

@Data
public class ClinicWaitingDTO {
    int id;
    int patientId;
    int employeeId;
    int typeId;
    String symptom;
    String status;
    Date entryDate;
}
