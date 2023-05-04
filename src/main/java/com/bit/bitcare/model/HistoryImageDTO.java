package com.bit.bitcare.model;

import lombok.Data;

import java.util.Date;

@Data
public class HistoryImageDTO {
    int id;
    int historyId;
    String imagePath;
    Date entryDate;
    String categoryId;
}
