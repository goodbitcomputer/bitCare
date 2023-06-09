package com.bit.bitcare.model;

import lombok.Data;

@Data
public class EmployeeDTO {
    int id;
    String phoneNumber;
    String name;
    int deptId;
    String role;
    String profileImage;
    String detail;
    String username;
    String password;
}
