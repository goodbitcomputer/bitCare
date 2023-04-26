package com.bit.bitcare.serviceImpl;

import com.bit.bitcare.dao.DiseaseDAO;
import com.bit.bitcare.dao.EmployeeDAO;
import com.bit.bitcare.lucene.DiseaseIndexer;
import com.bit.bitcare.model.DiseaseDTO;
import com.bit.bitcare.model.EmployeeDTO;
import com.bit.bitcare.model.UserCustomDetails;
import com.bit.bitcare.service.DoctorService;
import com.bit.bitcare.service.EmployeeService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DiseaseDAO diseaseDAO;
    private DiseaseIndexer diseaseIndexer;

    public DoctorServiceImpl(DiseaseDAO diseaseDAO, DiseaseIndexer diseaseIndexer) {
        this.diseaseDAO = diseaseDAO;
        this.diseaseIndexer = diseaseIndexer;
    }

    // 상병(더미)테이블 search
    @Override
    public JsonObject filterSearch(String filterMessage) {
        JsonObject result = new JsonObject();
        List<DiseaseDTO> list = diseaseDAO.search(filterMessage);


        /* 엘라스틱 서치
            List<DiseaseDTO> list = null;
            try {
                list = diseaseIndexer.search(filterMessage);
            } catch (ParseException | IOException e) {
                throw new RuntimeException(e);
            }
         */


        JsonArray array = new JsonArray();

        if (list == null) {
            result.addProperty("status", "fail");
        } else {
            for (DiseaseDTO d : list) {
                JsonObject object = new JsonObject();
                object.addProperty("id", d.getId());
                object.addProperty("code", d.getCode());
                object.addProperty("name", d.getName());

                array.add(object);
            }

            result.addProperty("list", array.toString());
            result.addProperty("status", "success");
        }

        return result;
    }
}

