package com.bit.bitcare.service;

import com.bit.bitcare.model.EmployeeDTO;

import java.util.List;

public interface EmployeeService {
    public EmployeeDTO auth(EmployeeDTO employeeDTO);
    public List<EmployeeDTO> selectAll();
    public boolean validate(String username);
    public EmployeeDTO selectOne(int id);
    public void update(EmployeeDTO employeeDTO);
    public void delete(int id);
    public boolean register(EmployeeDTO attempt);
}
