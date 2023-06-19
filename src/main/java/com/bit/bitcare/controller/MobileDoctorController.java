package com.bit.bitcare.controller;

import com.bit.bitcare.awsS3.AwsS3;
import com.bit.bitcare.awsS3.AwsS3Service;
import com.bit.bitcare.model.BodyCategoryDTO;
import com.bit.bitcare.model.DeptDTO;
import com.bit.bitcare.model.PatientDTO;
import com.bit.bitcare.service.MobileDoctorService;
import com.bit.bitcare.service.MobileService;
import com.bit.bitcare.serviceImpl.MobileServiceImpl;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/mobile/doctor")
public class MobileDoctorController {
    private MobileService mobileService;
    private MobileDoctorService mobileDoctorService;
    @Autowired
    private AwsS3Service awsS3Service;

    public MobileDoctorController(MobileDoctorService mobileDoctorService, MobileServiceImpl mobileService) {
        this.mobileService = mobileService;
        this.mobileDoctorService = mobileDoctorService;
    }


    @ResponseBody
    @PostMapping("/insertPatient")
    public String insertPatient(@RequestBody Map<String, Object> requestData, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        return mobileService.insertPatient(requestData).toString();
    }

    @ResponseBody
    @GetMapping("/getBodyCategoryData")
    public List<BodyCategoryDTO> getBodyCategoryData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return mobileDoctorService.getBodyCategoryAll();
    }

    @ResponseBody
    @PostMapping("/photoSave_proc")
    public Boolean photoSave(@RequestPart(value = "uploadFiles", required = true) MultipartFile[] files, @RequestPart(value = "bodyCategoryId", required = true) int bodyCategoryId, @RequestPart(value = "historyId", required = true) int historyId, HttpServletRequest request) throws IOException {
        Boolean result = false;
        for (MultipartFile multipartFile : files) {

            AwsS3 awsS3 = awsS3Service.upload(multipartFile, "imgUpload/" + historyId);

//            System.out.println("aws key: " +awsS3.getKey());      // aws 스토리지에 올라간 파일 key (삭제하려면 key가 필요함)
//            System.out.println("aws path: " +awsS3.getPath());    // aws 스토리지에 올라간 파일 path (url)

            result = mobileDoctorService.photoSave(awsS3, historyId, bodyCategoryId);

            // 이미지 삭제
//            AwsS3 removeS3 = new AwsS3();
//            removeS3.setKey(awsS3.getKey());
//            awsS3Service.remove(removeS3);

        }

        return result;
    }

}
