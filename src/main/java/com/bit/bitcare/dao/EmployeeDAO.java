package com.bit.bitcare.dao;

import com.bit.bitcare.model.EmployeeDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface EmployeeDAO {
    public EmployeeDTO auth(EmployeeDTO employeeDTO);
    public List<EmployeeDTO> selectAll();
    public EmployeeDTO validate(String username);
    public EmployeeDTO selectOne(int id);
    public void update(EmployeeDTO employeeDTO);
    public void delete(int id);
    public void register(EmployeeDTO attempt);
}
