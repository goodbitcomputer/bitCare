package com.bit.bitcare.serviceImpl;

import com.bit.bitcare.dao.EmployeeDAO;
import com.bit.bitcare.model.EmployeeDTO;
import com.bit.bitcare.model.UserCustomDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
/**
 * ---------------------------------------------------------------------------
 * 2023.05.16
 *
 * EmployeeDetailService
 * UserDetailsService 인터페이스에 대한 구현체
 * EmployeeDAO 객체 사용
 * 메소드 목록
 * 1. loadUserByUsername : username 을 받아 UserDetails 객체를 생성 후 리턴하는 메소드 UserDetails 는 SpringSecurity 에서 인증을 위한 객체로 사용된다.
 */
@Service
public class EmployeeDetailService implements UserDetailsService {
    private final EmployeeDAO employeeDAO;

    public EmployeeDetailService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        EmployeeDTO user = employeeDAO.validate(s);
        if (user != null) {
            return new UserCustomDetails(user);
        }
        return null;
    }
}
