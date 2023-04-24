package com.bit.bitcare.dao;

import com.bit.bitcare.model.DeptDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DeptDAO {
    public List<DeptDTO> selectAll();
    public DeptDTO selectOne(int id);
}
