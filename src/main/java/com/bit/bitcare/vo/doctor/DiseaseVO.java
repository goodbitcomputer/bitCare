package com.bit.bitcare.vo.doctor;

import lombok.Data;

@Data
public class DiseaseVO {
    int id;
    String code;
    String name;
    Boolean icon;
    Boolean main;
    Boolean sub;
}
