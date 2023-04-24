package com.bit.bitcare.dao;

import com.bit.bitcare.model.ClinicWaitingDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ClinicWaitingDAO {
    public ClinicWaitingDTO selectOne(int id);
    public List<ClinicWaitingDTO> selectAll();
    public void delete(int id);
    public void insert(ClinicWaitingDTO clinicWaitingDTO);
    public void update(ClinicWaitingDTO clinicWaitingDTO);
}
