package com.bit.bitcare.service;

import com.bit.bitcare.awsS3.AwsS3;
import com.bit.bitcare.model.BodyCategoryDTO;
import com.bit.bitcare.model.DeptDTO;
import com.bit.bitcare.model.PatientDTO;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

public interface MobileDoctorService {
    public List<BodyCategoryDTO> getBodyCategoryAll();
    public Boolean photoSave(AwsS3 awsS3, int historyId, int bodyCategoryId);
}
