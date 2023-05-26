package com.bit.bitcare.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class MessageDTO {
    int id;
    String sender;
    String receiver;
    String content;
    String messageFile;
    String receiveState;
    String sendState;
    Date entryDate;
}
