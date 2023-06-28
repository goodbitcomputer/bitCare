package com.bit.bitcare.dao;

import com.bit.bitcare.model.BodyCategoryDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BodyCategoryDAO {
    public BodyCategoryDTO selectOne(int id);

    public List<BodyCategoryDTO> selectAll();

    public BodyCategoryDTO loadBodyCategoryImage(int patientId, int historyId);
}
