package com.bit.bitcare.lucene;

import com.bit.bitcare.model.DiseaseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DiseaseRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<DiseaseDTO> findAll() {
        String sql = "SELECT * FROM disease";
        RowMapper<DiseaseDTO> rowMapper = (resultSet, i) -> {
            DiseaseDTO diseaseDTO = new DiseaseDTO();
            diseaseDTO.setId(resultSet.getInt("id"));
            diseaseDTO.setCode(resultSet.getString("code"));
            diseaseDTO.setName(resultSet.getString("name"));
            return diseaseDTO;
        };
        return jdbcTemplate.query(sql, rowMapper);
    }
    public List<DiseaseDTO> findByname(String name) {
        String sql = "SELECT id, name, content FROM disease WHERE name = ?";
        RowMapper<DiseaseDTO> rowMapper = (resultSet, i) -> {
            DiseaseDTO myDto = new DiseaseDTO();
            myDto.setId(resultSet.getInt("id"));
            myDto.setCode(resultSet.getString("content"));
            myDto.setName(resultSet.getString("name"));
            return myDto;
        };
        return jdbcTemplate.query(sql, rowMapper, name);
    }

}
