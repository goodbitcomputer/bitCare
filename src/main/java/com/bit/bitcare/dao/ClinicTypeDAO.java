package com.bit.bitcare.dao;

import com.bit.bitcare.model.ClinicTypeDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ClinicTypeDAO {
    public List<ClinicTypeDTO> selectAll();
    public ClinicTypeDTO selectOne(int id);
}
