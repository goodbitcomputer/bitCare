package com.bit.bitcare.serviceImpl;

import com.bit.bitcare.dao.*;
import com.bit.bitcare.lucene.DiseaseIndexer;
import com.bit.bitcare.model.*;
import com.bit.bitcare.service.DoctorService;
import com.bit.bitcare.vo.doctor.DiagnoseVO;
import com.bit.bitcare.vo.doctor.DiseaseVO;
import com.bit.bitcare.vo.doctor.UpsertVO;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DoctorServiceImpl implements DoctorService {

    private DiseaseIndexer diseaseIndexer;
    private final DiseaseDAO diseaseDAO;
    private final DiagnoseDAO diagnoseDAO;
    private final WaitingDAO waitingDAO;
    private final HistoryDAO historyDAO;
    private final HistoryDiseaseDAO historyDiseaseDAO;
    private final HistoryDiagnoseDAO historyDiagnoseDAO;
    private final HistoryImageDAO historyImageDAO;

    public DoctorServiceImpl(HistoryImageDAO historyImageDAO, HistoryDiagnoseDAO historyDiagnoseDAO, HistoryDiseaseDAO historyDiseaseDAO, HistoryDAO historyDAO, WaitingDAO waitingDAO, DiagnoseDAO diagnoseDAO, DiseaseDAO diseaseDAO, DiseaseIndexer diseaseIndexer) {
        this.diseaseIndexer = diseaseIndexer;
        this.diseaseDAO = diseaseDAO;
        this.diagnoseDAO = diagnoseDAO;
        this.waitingDAO = waitingDAO;
        this.historyDAO = historyDAO;
        this.historyDiseaseDAO = historyDiseaseDAO;
        this.historyDiagnoseDAO = historyDiagnoseDAO;
        this.historyImageDAO = historyImageDAO;
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

        List<WaitingDTO> list = waitingDAO.selectWaitByDept(logIn.getDeptId());

        return list;
    }

    @Override
    public List<WaitingDTO> getWaitingCmopletedData(HttpServletRequest request) {
        HttpSession session = request.getSession();
        EmployeeDTO logIn = (EmployeeDTO) session.getAttribute("logIn");

        List<WaitingDTO> list = waitingDAO.selectCompletedByDept(logIn.getDeptId());

        return list;
    }

    @Transactional
    @Override
    public JsonObject historyUpsert(UpsertVO requestData, HttpServletRequest request) {
        JsonObject result = new JsonObject();
        HttpSession session = request.getSession();

        try {
            EmployeeDTO logIn = (EmployeeDTO) session.getAttribute("logIn");
            HistoryDTO historyDTO = new HistoryDTO();
            if(requestData.getId() == -1) {
                historyDTO.setId(requestData.getId());
            }else{
                historyDTO = historyDAO.selectOne(requestData.getId());
            }

            historyDTO.setEmployeeId(logIn.getId());
            historyDTO.setPatientId(requestData.getPatientId());
            historyDTO.setDeptId(requestData.getDeptId());
            historyDTO.setMemo(requestData.getMemo());
            historyDTO.setBpSystolic(requestData.getBpSystolic());
            historyDTO.setBpDiastolic(requestData.getBpDiastolic());
            historyDTO.setHeight(requestData.getHeight());
            historyDTO.setWeight(requestData.getWeight());
            historyDTO.setTemperature(requestData.getTemperature());
            historyDTO.setSymptomDetail(requestData.getSymptomDetail());
            historyDTO.setVisit(requestData.getVisit());

            System.out.println(historyDTO);
            if(historyDTO.getId() ==-1) {
                historyDAO.insert(historyDTO);
            }else{
                System.out.println(historyDTO.getId());
                historyDAO.update(historyDTO);
//                업데이트시 히스토리관련 상병테이블 초기화
                List<HistoryDiseaseDTO> diseaseList = historyDiseaseDAO.selectByHistoryId(historyDTO.getId());
                System.out.println("diseaseList: "+diseaseList);
                for(HistoryDiseaseDTO diseaseDTO : diseaseList){
                    historyDiseaseDAO.delete(diseaseDTO.getId());
                }
//                업데이트시 히스토리관련 처방테이블 초기화
                List<HistoryDiagnoseDTO> diagnoseList = historyDiagnoseDAO.selectByHistoryId(historyDTO.getId());
                System.out.println("diagnoseList: "+diagnoseList);
                for(HistoryDiagnoseDTO diagnoseDTO : diagnoseList){
                    historyDiagnoseDAO.delete(diagnoseDTO.getId());
                }
            }
            
            // 상병 히스토리id 넣어야함
            for (DiseaseVO item : requestData.getWriteSbList()) {
                HistoryDiseaseDTO diseaseDTO = new HistoryDiseaseDTO();
                diseaseDTO.setHistoryId(historyDTO.getId());
                diseaseDTO.setCode(item.getCode());
                diseaseDTO.setName(item.getName());
                diseaseDTO.setDegree(item.getMain() ? "주상병" : "부상병");

                System.out.println(diseaseDTO);
                historyDiseaseDAO.insert(diseaseDTO);
            }

            // 처방 히스토리id 넣어야함
            for (DiagnoseVO item : requestData.getWriteCbList()) {
                HistoryDiagnoseDTO diagnoseDTO = new HistoryDiagnoseDTO();
                diagnoseDTO.setHistoryId(historyDTO.getId());
                diagnoseDTO.setCode(item.getCode());
                diagnoseDTO.setName(item.getName());
                diagnoseDTO.setDose(item.getDose());
                diagnoseDTO.setTime(item.getTime());
                diagnoseDTO.setDays(item.getDays());

                System.out.println(diagnoseDTO);
                historyDiagnoseDAO.insert(diagnoseDTO);
            }

            result.addProperty("status", "success");
        } catch (Exception e) {
            result.addProperty("status", "fail");
        }
        return result;
    }

    @Override
    public List<HistoryDTO> getHistoryList(int patientId) {
        return historyDAO.selectByPatientId(patientId);
    }

    @Override
    public JsonObject getHistoryAddData(int historyId) {
        JsonObject result = new JsonObject();

        List<HistoryDiseaseDTO> diseaseList = historyDiseaseDAO.selectByHistoryId(historyId);
        List<HistoryDiagnoseDTO> diagnoseList = historyDiagnoseDAO.selectByHistoryId(historyId);
        List<HistoryImageDTO> imgList = historyImageDAO.selectByHistoryId(historyId);

        JsonArray diseaseArray = new JsonArray();
        JsonArray diagnoseArray = new JsonArray();
        JsonArray imgArray = new JsonArray();

        for (HistoryDiseaseDTO d : diseaseList) {
            JsonObject object = new JsonObject();
            object.addProperty("historyId", d.getHistoryId());
            object.addProperty("degree", d.getDegree());
            object.addProperty("code", d.getCode());
            object.addProperty("name", d.getName());

            diseaseArray.add(object);
        }
        for (HistoryDiagnoseDTO d : diagnoseList) {
            JsonObject object = new JsonObject();
            object.addProperty("historyId", d.getHistoryId());
            object.addProperty("code", d.getCode());
            object.addProperty("name", d.getName());
            object.addProperty("dose", d.getDose());
            object.addProperty("time", d.getTime());
            object.addProperty("days", d.getDays());

            diagnoseArray.add(object);
        }
        for (HistoryImageDTO i : imgList) {
            JsonObject object = new JsonObject();
            object.addProperty("historyId", i.getHistoryId());
            object.addProperty("imagePath", i.getImagePath());
            object.addProperty("imageKey", i.getImageKey());
            object.addProperty("categoryId", i.getCategoryId());
            object.addProperty("entryDate", i.getEntryDate().toString());

            imgArray.add(object);
        }

        result.addProperty("diseaseList", diseaseArray.toString());
        result.addProperty("diagnoseList", diagnoseArray.toString());
        result.addProperty("imgList", imgArray.toString());



        return result;
    }

    @Override
    public void completedWaiting(WaitingDTO waitingDTO) {
        waitingDAO.update(waitingDTO);
    }


}

