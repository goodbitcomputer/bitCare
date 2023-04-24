package com.bit.bitcare.model;

import lombok.Data;

@Data
public class PatientDTO {
    String phoneNumber;
    String identityNumber;
    String birth;
    String gender;
    String name;
    int addressId;
}
