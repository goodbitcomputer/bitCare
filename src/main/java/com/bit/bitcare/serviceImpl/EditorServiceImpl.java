package com.bit.bitcare.serviceImpl;

import com.bit.bitcare.awsS3.AwsS3;
import com.bit.bitcare.awsS3.AwsS3Service;
import com.bit.bitcare.dao.HistoryDAO;
import com.bit.bitcare.dao.HistoryImageDAO;
import com.bit.bitcare.model.HistoryImageDTO;
import com.bit.bitcare.service.EditorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EditorServiceImpl implements EditorService {
    private final HistoryImageDAO historyImageDAO;
    private final HistoryDAO historyDAO;
    private final AwsS3Service awsS3Service;

    public EditorServiceImpl(HistoryImageDAO historyImageDAO, HistoryDAO historyDAO, AwsS3Service awsS3Service) {
        this.historyImageDAO = historyImageDAO;
        this.historyDAO = historyDAO;
        this.awsS3Service = awsS3Service;
    }


    @Override
    public List<HistoryImageDTO> getImagesByPatientIdAndHistoryId(int patientId, int historyId) {
        System.out.println(patientId);
        System.out.println(historyId);
        System.out.println(historyImageDAO.selectByPatientIdAndHistoryId(patientId, historyId));
        return historyImageDAO.selectByPatientIdAndHistoryId(patientId, historyId);
    }


    @Override
    public void saveEditedPhoto(AwsS3 awsS3, int historyId, int bodyCategoryId, int edited) {

        HistoryImageDTO imageDTO = new HistoryImageDTO();
        imageDTO.setHistoryId(historyId);
        imageDTO.setImagePath(awsS3.getPath());
        imageDTO.setImageKey(awsS3.getKey());
        imageDTO.setCategoryId(bodyCategoryId);
        imageDTO.setEdited(edited);

        historyImageDAO.insertEditedPhoto(imageDTO);
    }

}