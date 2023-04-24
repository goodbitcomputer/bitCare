package com.bit.bitcare.model;

import lombok.Data;

import java.util.Date;

@Data
public class HistoryDTO {
    int employeeId;
    int patientId;
    int deptId;
    String memo;
    int bpSystolic;
    int bpDiastolic;
    int height;
    int weight;
    int temperature;
    String symptom_detail;
    Date entryDate;
    String visit;
}
