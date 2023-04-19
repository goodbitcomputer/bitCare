package com.bit.bitcare.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class MemberController {
    @GetMapping("/member")
    public String showMember(){
        return "member/meberInfo";
    }
}
