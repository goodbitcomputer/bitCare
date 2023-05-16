package com.bit.bitcare.serviceImpl;

import com.bit.bitcare.dao.EmployeeDAO;
import com.bit.bitcare.model.EmployeeDTO;
import com.bit.bitcare.model.UserCustomDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
