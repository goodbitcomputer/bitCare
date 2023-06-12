package com.bit.bitcare.model;

import lombok.Data;

@Data
public class VisitDTO {
    int id;
    String code;
    String content;
    int payment;
    String type;
}
