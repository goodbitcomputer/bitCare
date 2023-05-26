package com.bit.bitcare.vo.doctor;

import lombok.Data;

@Data
public class DiagnoseVO {
    int id;
    String code;
    String name;
    int dose;
    int time;
    int days;
    Boolean icon;
}
