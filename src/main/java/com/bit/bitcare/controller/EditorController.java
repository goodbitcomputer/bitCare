package com.bit.bitcare.controller;

import com.bit.bitcare.awsS3.AwsS3;
import com.bit.bitcare.awsS3.AwsS3Service;
import com.bit.bitcare.dao.HistoryDAO;
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

    @Autowired
    public EditorController(HistoryDAO historyDAO, EditorService editorService, AwsS3Service awsS3Service) {
        this.editorService = editorService;
        this.awsS3Service = awsS3Service;
        this.historyDAO = historyDAO;
    }

    @ResponseBody
    @PostMapping("/selectByPatientIdAndHistoryId")
    public List<HistoryImageDTO> selectByPatientIdAndHistoryId(@RequestParam int patientId, @RequestParam int historyId) {
        System.out.println(patientId);
        System.out.println(historyId);
        List<HistoryImageDTO> imagePaths = editorService.getImagesByPatientIdAndHistoryId(patientId, historyId);
        return imagePaths;
    }

    @ResponseBody
    @PostMapping("/saveEditedPhoto")
    public Boolean saveEditedPhoto(@RequestPart(value = "uploadFile", required = true) MultipartFile file,
                                   @RequestPart(value = "historyId", required = true) int historyId,
                                   @RequestPart(value = "bodyCategoryId", required = true) int bodyCategoryId,
                                   @RequestPart(value = "edited", required = true) int edited,
                                   HttpServletRequest request) throws IOException {

        HistoryDTO historyDTO = historyDAO.selectOne(historyId);

        AwsS3 awsS3 = awsS3Service.upload(file, "imgUpload/" + historyDTO.getPatientId());

        // 이미지 경로를 DB에 저장
        editorService.saveEditedPhoto(awsS3, historyId, bodyCategoryId, edited);


        return true;
    }


}