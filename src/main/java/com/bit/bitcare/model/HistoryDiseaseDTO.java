package com.bit.bitcare.model;

import lombok.Data;

@Data
public class HistoryDiseaseDTO {
    int id;
    int historyId;
    String degree;
    String code;
    String name;
}
