package com.bit.bitcare.controller;

import com.bit.bitcare.awsS3.AwsS3;
import com.bit.bitcare.awsS3.AwsS3Service;
import com.bit.bitcare.dao.BodyCategoryDAO;
import com.bit.bitcare.dao.HistoryDAO;
import com.bit.bitcare.dao.HistoryImageDAO;
import com.bit.bitcare.model.BodyCategoryDTO;
import com.bit.bitcare.model.HistoryDTO;
import com.bit.bitcare.model.HistoryImageDTO;
import com.bit.bitcare.service.EditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/doctor/editor")
public class EditorController {
    private final EditorService editorService;
    private final AwsS3Service awsS3Service;
    private final HistoryDAO historyDAO;
    private final HistoryImageDAO historyImageDAO;
    private final BodyCategoryDAO bodyCategoryDAO;

    @Autowired
    public EditorController(HistoryImageDAO historyImageDAO, HistoryDAO historyDAO, EditorService editorService, AwsS3Service awsS3Service, BodyCategoryDAO bodyCategoryDAO) {
        this.editorService = editorService;
        this.awsS3Service = awsS3Service;
        this.historyDAO = historyDAO;
        this.historyImageDAO = historyImageDAO;
        this.bodyCategoryDAO = bodyCategoryDAO;
    }

    @ResponseBody
    @PostMapping("/selectByPatientIdAndHistoryId")
    public List<HistoryImageDTO> selectByPatientIdAndHistoryId(@RequestParam int patientId, @RequestParam int historyId) {
//        System.out.println(patientId);
//        System.out.println(historyId);
        List<HistoryImageDTO> imagePaths = editorService.getImagesByPatientIdAndHistoryId(patientId, historyId);
        return imagePaths;
    }

    @ResponseBody
    @PostMapping("/saveEditedImage")
    public Boolean saveEditedImage(@RequestPart(value = "uploadFile", required = true) MultipartFile file,
                                   @RequestPart(value = "historyId", required = true) int historyId,
                                   @RequestPart(value = "bodyCategoryId", required = true) int bodyCategoryId,
                                   @RequestPart(value = "edited", required = true) int edited,
                                   HttpServletRequest request) throws IOException {

        HistoryDTO historyDTO = historyDAO.selectOne(historyId);

        AwsS3 awsS3 = awsS3Service.upload(file, "imgUpload/" + historyDTO.getPatientId());

        // 이미지 경로를 DB에 저장
        editorService.saveEditedImage(awsS3, historyId, bodyCategoryId, edited);


        return true;
    }

    @ResponseBody
    @PostMapping("/deleteImage")
    public void deleteImage(String imagePath) {

//        AwsS3 awsS3 = awsS3Service.remove(File file);

        HistoryImageDTO historyImageDTO = historyImageDAO.selectByImagePath(imagePath);
        AwsS3 removeS3 = new AwsS3();
        removeS3.setKey(historyImageDTO.getImageKey());

        editorService.deleteImage(imagePath);
        awsS3Service.remove(removeS3);
    }

    @ResponseBody
    @PostMapping("/updateEditedImage")
    public void updateEditedImage(@RequestPart(value = "uploadFile", required = true) MultipartFile file, HistoryImageDTO historyImageDTO) throws IOException {
        HistoryDTO historyDTO = historyDAO.selectOne(historyImageDTO.getHistoryId());
        HistoryImageDTO historyImageTemp = historyImageDAO.selectById(historyImageDTO.getId());
        // s3 이미지 삭제
//        AwsS3 awsS3 = awsS3Service.remove(File file);

        AwsS3 awsS3 = awsS3Service.upload(file, "imgUpload/" + historyDTO.getPatientId());

        AwsS3 removeS3 = new AwsS3();
        removeS3.setKey(historyImageTemp.getImageKey());
        awsS3Service.remove(removeS3);

        editorService.updateEditedImage(awsS3, historyImageTemp);

    }


    @ResponseBody
    @PostMapping("/loadBodyCategoryImage")
    public BodyCategoryDTO loadBodyCategoryImage(@RequestParam int patientId, @RequestParam int historyId) {
        System.out.println(bodyCategoryDAO.loadBodyCategoryImage(patientId, historyId));
        return bodyCategoryDAO.loadBodyCategoryImage(patientId, historyId);
    }


}