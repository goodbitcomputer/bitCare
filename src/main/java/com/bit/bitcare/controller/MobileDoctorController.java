package com.bit.bitcare.controller;

import com.bit.bitcare.awsS3.AwsS3;
import com.bit.bitcare.awsS3.AwsS3Service;
import com.bit.bitcare.model.DeptDTO;
import com.bit.bitcare.model.PatientDTO;
import com.bit.bitcare.service.MobileService;
import com.bit.bitcare.serviceImpl.MobileServiceImpl;
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
    @Autowired
    private AwsS3Service awsS3Service;

    public MobileDoctorController(MobileServiceImpl mobileService) {
        this.mobileService = mobileService;
    }


    @ResponseBody
    @PostMapping("/insertPatient")
    public String insertPatient(@RequestBody Map<String, Object> requestData, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        return mobileService.insertPatient(requestData).toString();
    }

    @ResponseBody
    @PostMapping("/photoSave_proc")
    public String photoSave(@RequestPart(value="uploadFiles", required = true) MultipartFile[] files, HttpServletRequest request ) throws IOException {
//        System.out.println("file: "+ file);
        for(MultipartFile multipartFile : files) {


            AwsS3 awsS3 = awsS3Service.upload(multipartFile,"imgUpload");

            System.out.println("aws key: " +awsS3.getKey());
            System.out.println("aws path: " +awsS3.getPath());

            AwsS3 removeS3 = new AwsS3();

            removeS3.setKey(awsS3.getKey());

// 이미지 삭제
//            awsS3Service.remove(removeS3);

        }

        return null;
    }


}
