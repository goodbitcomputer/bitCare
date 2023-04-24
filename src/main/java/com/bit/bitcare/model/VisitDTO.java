package com.bit.bitcare.model;

import lombok.Data;

@Data
public class VisitDTO {
    int code;
    String content;
    int payment;
    String type;
}
