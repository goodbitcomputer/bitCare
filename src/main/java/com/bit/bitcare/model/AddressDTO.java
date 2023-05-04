package com.bit.bitcare.model;

import lombok.Data;

@Data
public class AddressDTO {
    int id;
    String zipCode;
    String roadAddress;
    String streetAddress;
    String extraAddress;

}
