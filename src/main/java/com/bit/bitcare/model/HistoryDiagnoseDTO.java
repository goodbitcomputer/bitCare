package com.bit.bitcare.model;

import lombok.Data;

@Data
public class HistoryDiagnoseDTO {
    int id;
    int historyId;
    String code;
    String name;
    int dose;
    int time;
    int days;
}
