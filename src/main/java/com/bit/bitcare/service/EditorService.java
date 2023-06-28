package com.bit.bitcare.service;

import com.bit.bitcare.awsS3.AwsS3;
import com.bit.bitcare.model.BodyCategoryDTO;
import com.bit.bitcare.model.HistoryImageDTO;

import java.util.List;

public interface EditorService {
    List<HistoryImageDTO> getImagesByPatientIdAndHistoryId(int patientId, int historyId);

    void saveEditedImage(AwsS3 awsS3, int historyId, int bodyCategoryId, int edited);

    void deleteImage(String imagePath);

    void updateEditedImage(AwsS3 awsS3, HistoryImageDTO historyImageDTO);

    BodyCategoryDTO loadBodyCategoryImage(int patientId, int historyId);

}