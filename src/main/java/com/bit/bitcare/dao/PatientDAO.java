package com.bit.bitcare.dao;

import com.bit.bitcare.model.PatientDTO;
import com.bit.bitcare.model.SearchDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PatientDAO {
    public PatientDTO selectOne(int id);
    public PatientDTO selectByPhone(String phoneNumber);
    public PatientDTO selectByIdentity(String identityNumber);
    public List<PatientDTO> selectAll();
    public void delete(int id);
    public void insert(PatientDTO patientDTO);
    public List<PatientDTO> searchAll(SearchDTO searchDTO);
    public int count(SearchDTO searchDTO);
}
