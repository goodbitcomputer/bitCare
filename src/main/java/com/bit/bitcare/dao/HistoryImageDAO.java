package com.bit.bitcare.dao;

import com.bit.bitcare.model.HistoryImageDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface HistoryImageDAO {
    public HistoryImageDTO selectById(int id);
    public List<HistoryImageDTO> selectByHistoryId(int historyId);
    public void insert(HistoryImageDTO historyImageDTO);

    public List<HistoryImageDTO> selectByPatientIdAndHistoryId(int patientId, int historyId);

    public void insertEditedImage(HistoryImageDTO historyImageDTO);

    void deleteImage(String imagePath);

    public void update(HistoryImageDTO historyImageDTO);
}
