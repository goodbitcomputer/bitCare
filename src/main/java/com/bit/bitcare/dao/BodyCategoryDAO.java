package com.bit.bitcare.dao;

import com.bit.bitcare.model.BodyCategoryDTO;
import org.springframework.stereotype.Component;

@Component
public interface BodyCategoryDAO {
    public BodyCategoryDTO selectOne(int id);
}
