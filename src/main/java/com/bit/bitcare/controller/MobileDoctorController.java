package com.bit.bitcare.controller;

import com.bit.bitcare.model.DeptDTO;
import com.bit.bitcare.model.PatientDTO;
import com.bit.bitcare.service.MobileService;
import com.bit.bitcare.serviceImpl.MobileServiceImpl;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
        for(MultipartFile file : files){
            System.out.println("file: "+ file);

            // 파일 값 받기
//        MultipartFile multipartFile = request.getFile("file");
            /* 파일 이름 변경 */
            UUID uuid = UUID.randomUUID();
            String filename = uuid + "_" + file.getOriginalFilename();


//            Path dir = Paths.get(request.getSession().getServletContext().getRealPath("/resources/upload"));
//
//            System.out.println(dir);

            Path dir = new ClassPathResource("uploads").getFile().toPath();
            dir = Paths.get(dir.toString()+"/history_1");

            try {
                // 디렉토리 생성
                Files.createDirectory(dir);

                System.out.println(dir + " 디렉토리가 생성되었습니다.");

            } catch (FileAlreadyExistsException e) {
                System.out.println("디렉토리가 이미 존재합니다");
            } catch (NoSuchFileException e) {
                System.out.println("디렉토리 경로가 존재하지 않습니다");
            }catch (IOException e) {
                e.printStackTrace();
            }

            File f = new File(dir.toRealPath() + filename);


            System.out.println(filename);
            System.out.println(f.getPath());
            f.deleteOnExit();
            f.createNewFile();
            /* 실제 폴더에 파일 업로드 */
            file.transferTo(f);
//        productDTO.setImg("/resources/upload/"+filename);

        }

        return null;
    }


}
