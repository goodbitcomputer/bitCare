package com.bit.bitcare.serviceImpl;

import com.bit.bitcare.dao.*;
import com.bit.bitcare.lucene.DiseaseIndexer;
import com.bit.bitcare.model.*;
import com.bit.bitcare.service.NurseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NurseServiceImpl implements NurseService {

    private DiseaseIndexer diseaseIndexer;
    private final DiseaseDAO diseaseDAO;
    private final DiagnoseDAO diagnoseDAO;
    private final WaitingDAO waitingDAO;
    private final HistoryDAO historyDAO;
    private final HistoryDiseaseDAO historyDiseaseDAO;
    private final HistoryDiagnoseDAO historyDiagnoseDAO;
    private final HistoryImageDAO historyImageDAO;
    private final VisitDAO visitDAO;
    private final ReceiptDAO receiptDAO;
    private final PatientDAO patientDAO;
    private final ObjectMapper objectMapper;

    public NurseServiceImpl(ReceiptDAO receiptDAO, VisitDAO visitDAO, HistoryImageDAO historyImageDAO, HistoryDiagnoseDAO historyDiagnoseDAO, HistoryDiseaseDAO historyDiseaseDAO, HistoryDAO historyDAO, WaitingDAO waitingDAO, DiagnoseDAO diagnoseDAO, DiseaseDAO diseaseDAO, PatientDAO patientDAO, ObjectMapper objectMapper, DiseaseIndexer diseaseIndexer) {
        this.diseaseIndexer = diseaseIndexer;
        this.objectMapper = objectMapper;
        this.diseaseDAO = diseaseDAO;
        this.diagnoseDAO = diagnoseDAO;
        this.waitingDAO = waitingDAO;
        this.historyDAO = historyDAO;
        this.historyDiseaseDAO = historyDiseaseDAO;
        this.historyDiagnoseDAO = historyDiagnoseDAO;
        this.historyImageDAO = historyImageDAO;
        this.visitDAO = visitDAO;
        this.receiptDAO = receiptDAO;
        this.patientDAO = patientDAO;
    }

    @Override
    public List<WaitingDTO> getWaitingData(HttpServletRequest request) {
        return waitingDAO.selectWaitAll();
    }

    @Override
    public List<WaitingDTO> getWaitingCmopletedData(HttpServletRequest request) {
        return waitingDAO.selectCompletedAll();
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
    public List<VisitDTO> getVisitByType(String type) {
        List<VisitDTO> list = visitDAO.selectByType(type);
        return list;
    }

    @Override
    public ReceiptDTO getReceiptData(Map<String, Object> requestData) {
        int historyId = (int) requestData.get("historyId");
        return receiptDAO.selectByHistoryId(historyId);
    }

    @Override
    public Boolean payment(Map<String, Object> requestData) {
        try {
            int patientId = (int) requestData.get("patientId");
            int historyId = (int) requestData.get("historyId");
            String content = requestData.get("content").toString();
            int payment = (int) requestData.get("payment");

            System.out.println("patientId: " + patientId);
            System.out.println("historyId: " + historyId);
            System.out.println("content: " + content);
            System.out.println("payment: " + payment);

            ReceiptDTO receiptDTO = new ReceiptDTO();
            receiptDTO.setPatientId(patientId);
            receiptDTO.setHistoryId(historyId);
            receiptDTO.setContent(content);
            receiptDTO.setPayment(payment);

            receiptDAO.insert(receiptDTO);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Transactional
    @Override
    public String canclePayment(Map<String, Object> requestData) {
        try {
            int receiptId = (int) requestData.get("receiptId");
            if (receiptDAO.selectOne(receiptId).getImpUid() == null) {
                receiptDAO.delete(receiptId);
            } else {
                return "결제완료된내역";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "요청완료된내역";
        }
        return "결제요청완료";
    }

    @Override
    public ResponseEntity<String> selectOne(int patientId) throws IOException {
        // JSON 데이터 생성
        Map<String, Object> data = new HashMap<>();

        PatientDTO selectPatient = patientDAO.selectOne(patientId);

        data.put("selectPatient", selectPatient);

        // JSON 문자열 생성
        String json = objectMapper.writeValueAsString(data);

        // HTTP 응답 생성
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }
}

