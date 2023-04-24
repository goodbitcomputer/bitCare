package com.bit.bitcare.dao;

import com.bit.bitcare.model.VisitDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface VisitDAO {
    public List<VisitDTO> selectByType(String type);
    public VisitDTO selectOne(int id);
}
