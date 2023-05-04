package com.bit.bitcare.serviceImpl;

import com.bit.bitcare.dao.AddressDAO;
import com.bit.bitcare.dao.DeptDAO;
import com.bit.bitcare.dao.PatientDAO;
import com.bit.bitcare.dao.WaitingDAO;
import com.bit.bitcare.model.AddressDTO;
import com.bit.bitcare.model.DeptDTO;
import com.bit.bitcare.model.PatientDTO;
import com.bit.bitcare.model.WaitingDTO;
import com.bit.bitcare.service.MobileService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class MobileServiceImpl implements MobileService {

    private final DeptDAO deptDAO;
    private final PatientDAO patientDAO;
    private final AddressDAO addressDAO;
    private final WaitingDAO waitingDAO;

    public MobileServiceImpl(DeptDAO deptDAO, PatientDAO patientDAO, AddressDAO addressDAO, WaitingDAO waitingDAO) {
        this.deptDAO = deptDAO;
        this.patientDAO = patientDAO;
        this.addressDAO = addressDAO;
        this.waitingDAO = waitingDAO;
    }

    // 진료과목 선택 페이지
    @Override
    public List<DeptDTO> getDeptAll() {
        List<DeptDTO> list = deptDAO.selectAll();
        return list;
    }

    @Override
    public PatientDTO auth(Map<String, Object> requestData) {
        PatientDTO patientDTO = null;
        try {
            Boolean isPhoneCheck = (Boolean) requestData.get("isPhoneCheck");
            String phoneNumber = (String) requestData.get("phoneNumber");
            String identityNumber = (String) requestData.get("identityNumber");

            if (isPhoneCheck == true) {
                patientDTO = patientDAO.selectByPhone(phoneNumber);
            } else {
                patientDTO = patientDAO.selectByIdentity(identityNumber);
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
        return patientDTO;
    }

    @Override
    public JsonObject insertPatient(Map<String, Object> requestData) {
        JsonObject result = new JsonObject();

        try {
            String phoneNumber = (String) requestData.get("phoneNumber");
            String identityNumber = (String) requestData.get("identityNumber");
            String name = (String) requestData.get("name");
            String postcode = (String) requestData.get("postcode");
            String roadAddress = (String) requestData.get("roadAddress");
            String streetAddress = (String) requestData.get("streetAddress");
            String extraAddress = (String) requestData.get("extraAddress");

            // 생년월일 변환
            DateFormat sdf = new SimpleDateFormat("yyMMdd");
            Date birth = sdf.parse(identityNumber.substring(0, 6));

            // 성별
            String gender = Integer.parseInt(identityNumber.substring(6, 7)) == 1 ? "남" : "여";

            // 주소 등록
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setZipCode(postcode);
            addressDTO.setRoadAddress(roadAddress);
            addressDTO.setStreetAddress(streetAddress);
            addressDTO.setExtraAddress(extraAddress);
            addressDAO.insert(addressDTO);

            // 환자 등록
            PatientDTO patientDTO = new PatientDTO();
            patientDTO.setPhoneNumber(phoneNumber);
            patientDTO.setIdentityNumber(identityNumber);
            patientDTO.setName(name);
            patientDTO.setGender(gender);
            patientDTO.setBirth(birth);
            patientDTO.setAddressId(addressDTO.getId());
            patientDAO.insert(patientDTO);

            result.addProperty("patientId", patientDTO.getId());

        } catch (ParseException e) {
//            e.printStackTrace();
            result.addProperty("status", "fail");
            return result;
        }

        result.addProperty("status", "success");
        return result;
    }

    @Override
    public Boolean register(Map<String, Object> requestData) {

        try {
            int patientId = (int) requestData.get("patientId");
            int deptId = (int) requestData.get("deptId");
            String symptom = (String) requestData.get("symptom");

            WaitingDTO waitingDTO = new WaitingDTO();
            waitingDTO.setPatientId(patientId);
            waitingDTO.setDeptId(deptId);
            waitingDTO.setSymptom(symptom);

            waitingDAO.insert(waitingDTO);

        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }

        return true;
    }
}

