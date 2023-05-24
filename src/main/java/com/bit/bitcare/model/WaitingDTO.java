package com.bit.bitcare.model;

import lombok.Data;

import java.util.Date;

@Data
public class WaitingDTO {
    int id;
    int patientId;
    int deptId;
    String symptom;
    Date entryDate;
    Date reservationDate;
    String state;

    /*
     * ------------------------------------------------------------
     * 2023.05.17 홍사범
     *
     * waitingDTO, patientDTO join 하기위해 수정
     * ------------------------------------------------------------
    */
    String name;
    String phoneNumber;
    String identityNumber;
    Date birth;
    String gender;
    int addressId;
}
