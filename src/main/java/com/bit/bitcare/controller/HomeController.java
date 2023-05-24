package com.bit.bitcare.controller;

import com.bit.bitcare.lucene.DiseaseIndexer;
import com.bit.bitcare.model.DiseaseDTO;
import com.bit.bitcare.model.EmployeeDTO;
import com.bit.bitcare.service.EmployeeService;
import com.bit.bitcare.serviceImpl.EmployeeServiceImpl;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private EmployeeService employeeService;

    public HomeController(EmployeeServiceImpl employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/test")
    public String showIndex() throws IOException {
//        System.out.println("여긴 테스트 구역");
//
//
//        List<EmployeeDTO> employeeList = employeeService.selectAll();
//
//        for(EmployeeDTO u : employeeList) {
//            System.out.println(u);
//        }

        return "index.html";


//        return "redirect:/mypage/showBasket";
    }

    public String getErrorPath() {
        return "/error";
    }
}
