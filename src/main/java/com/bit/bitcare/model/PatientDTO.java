package com.bit.bitcare.model;

import lombok.Data;

import java.util.Date;

@Data
public class PatientDTO {
    int id;
    String phoneNumber;
    String identityNumber;
    Date birth;
    String gender;
    String name;
    int addressId;
}
