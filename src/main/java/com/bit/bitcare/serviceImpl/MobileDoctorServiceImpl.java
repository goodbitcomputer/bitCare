package com.bit.bitcare.serviceImpl;

import com.bit.bitcare.awsS3.AwsS3;
import com.bit.bitcare.dao.BodyCategoryDAO;
import com.bit.bitcare.dao.HistoryDAO;
import com.bit.bitcare.dao.HistoryImageDAO;
import com.bit.bitcare.model.BodyCategoryDTO;
import com.bit.bitcare.model.HistoryDTO;
import com.bit.bitcare.model.HistoryImageDTO;
import com.bit.bitcare.service.MobileDoctorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MobileDoctorServiceImpl implements MobileDoctorService {
        private final BodyCategoryDAO bodyCategoryDAO;
        private final HistoryImageDAO historyImageDAO;
        private final HistoryDAO historyDAO;

    public MobileDoctorServiceImpl(HistoryDAO historyDAO, HistoryImageDAO historyImageDAO, BodyCategoryDAO bodyCategoryDAO) {
        this.bodyCategoryDAO = bodyCategoryDAO;
        this.historyImageDAO = historyImageDAO;
        this.historyDAO = historyDAO;
    }

    @Override
    public List<BodyCategoryDTO> getBodyCategoryAll() {
        List<BodyCategoryDTO> list = bodyCategoryDAO.selectAll();
        return list;
    }

    @Transactional
    @Override
    public Boolean photoSave(AwsS3 awsS3, int historyId, int bodyCategoryId, String memo) {
        try {
            HistoryDTO historyDTO = historyDAO.selectOne(historyId);
            historyDTO.setMemo(memo);
            historyDAO.update(historyDTO);

            HistoryImageDTO imageDTO = new HistoryImageDTO();
            imageDTO.setHistoryId(historyId);
            imageDTO.setImagePath(awsS3.getPath());
            imageDTO.setImageKey(awsS3.getKey());
            imageDTO.setCategoryId(bodyCategoryId);
            historyImageDAO.insert(imageDTO);

            HistoryImageDTO temp = historyImageDAO.selectById(imageDTO.getId());
            temp.setEdited(imageDTO.getId());

            historyImageDAO.update(temp);
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

}

