package com.bit.bitcare.dao;

import com.bit.bitcare.model.DiagnoseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DiagnoseDAO {
    public List<DiagnoseDTO> search(String keyword);
    public DiagnoseDTO selectOne(int id);
}
