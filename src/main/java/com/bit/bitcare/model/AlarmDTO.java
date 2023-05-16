package com.bit.bitcare.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class AlarmDTO {
    int id;
    String sender;
    String receiver;
    String content;
    String type;
    Date entryDate;
    String state;
}
