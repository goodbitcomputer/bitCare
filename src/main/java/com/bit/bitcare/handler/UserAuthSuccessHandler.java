package com.bit.bitcare.handler;

import com.bit.bitcare.model.UserCustomDetails;
import com.bit.bitcare.model.EmployeeDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Service
public class UserAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        EmployeeDTO employeeDTO = ((UserCustomDetails) authentication.getPrincipal()).getEmployeeDTO();
        employeeDTO.setPassword(null);
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("logIn", employeeDTO);

        System.out.println("UserAuthSuccessHandler.onAuthenticationSuccess");

        System.out.println("login: "+ employeeDTO);

//        httpServletResponse.sendRedirect("/");

    }
}