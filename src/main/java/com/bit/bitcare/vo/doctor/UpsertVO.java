package com.bit.bitcare.vo.doctor;

import lombok.Data;

@Data
public class UpsertVO {
    int id;
    int patientId;
    int deptId;
    String memo;
    int bpSystolic;
    int bpDiastolic;
    double height;
    double weight;
    double temperature;
    String symptomDetail;
    String visit;
    DiseaseVO[] writeSbList;
    DiagnoseVO[] writeCbList;
}
