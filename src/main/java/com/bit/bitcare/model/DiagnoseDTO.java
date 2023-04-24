package com.bit.bitcare.model;

import lombok.Data;

@Data
public class DiagnoseDTO {
    int code;
    String name;
    int dose;
    int time;
    int days;
}
