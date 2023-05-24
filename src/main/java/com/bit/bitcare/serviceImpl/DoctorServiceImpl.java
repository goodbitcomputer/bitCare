package com.bit.bitcare.serviceImpl;

import com.bit.bitcare.dao.DiagnoseDAO;
import com.bit.bitcare.dao.DiseaseDAO;
import com.bit.bitcare.dao.EmployeeDAO;
import com.bit.bitcare.dao.WaitingDAO;
import com.bit.bitcare.lucene.DiseaseIndexer;
import com.bit.bitcare.model.*;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    private DiseaseIndexer diseaseIndexer;
    private final DiseaseDAO diseaseDAO;
    private final DiagnoseDAO diagnoseDAO;
    private final WaitingDAO waitingDAO;

    public DoctorServiceImpl(WaitingDAO waitingDAO, DiagnoseDAO diagnoseDAO, DiseaseDAO diseaseDAO, DiseaseIndexer diseaseIndexer) {
        this.diseaseIndexer = diseaseIndexer;
        this.diseaseDAO = diseaseDAO;
        this.diagnoseDAO = diagnoseDAO;
        this.waitingDAO = waitingDAO;
    }

    // 상병(더미)테이블 search
    @Override
    public JsonObject sbFilterSearch(String filterMessage) {
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

    // 처방(더미)테이블 search
    @Override
    public List<DiagnoseDTO> cbFilterSearch(String filterMessage) {
        List<DiagnoseDTO> list = diagnoseDAO.search(filterMessage);

        return list;
    }

    @Override
    public List<WaitingDTO> getWaitingData(HttpServletRequest request) {
        HttpSession session = request.getSession();
        EmployeeDTO logIn = (EmployeeDTO) session.getAttribute("logIn");

        List<WaitingDTO> list = waitingDAO.selectAllByDept(logIn.getDeptId());

        return list;
    }



}

