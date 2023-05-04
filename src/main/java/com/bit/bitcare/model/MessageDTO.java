package com.bit.bitcare.model;

import lombok.Data;

@Data
public class MessageDTO {
    int id;
    String sender;
    String receiver;
    String content;
    String messageFile;
    String state;
}
