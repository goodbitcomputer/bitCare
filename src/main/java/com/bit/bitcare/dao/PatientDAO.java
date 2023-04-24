package com.bit.bitcare.dao;

import com.bit.bitcare.model.PatientDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PatientDAO {
    public PatientDTO selectOne(int id);
    public List<PatientDTO> selectAll();
    public void delete(int id);
    public void insert(PatientDTO patientDTO);
}
