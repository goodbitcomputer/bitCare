package com.bit.bitcare.dao;

import com.bit.bitcare.model.DiseaseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DiseaseDAO {
    public List<DiseaseDTO> search(String keyword);
    public DiseaseDTO selectOne(int id);
}
