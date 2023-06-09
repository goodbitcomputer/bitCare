package com.bit.bitcare.model;

import lombok.Data;

import java.util.Date;

@Data
public class HistoryDTO {
    int id;
    int employeeId;
    int patientId;
    int deptId;
    String memo;
    int bpSystolic;
    int bpDiastolic;
    double height;
    double weight;
    double temperature;
    String symptomDetail;
    Date entryDate;
    String visit;
    String dept;
    String name;
    String diseaseName;
}
