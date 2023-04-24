package com.bit.bitcare.serviceImpl;

import com.bit.bitcare.dao.EmployeeDAO;
import com.bit.bitcare.model.UserCustomDetails;
import com.bit.bitcare.model.EmployeeDTO;
import com.bit.bitcare.service.EmployeeService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements UserDetailsService, EmployeeService {

    private final EmployeeDAO employeeDAO;

    private BCryptPasswordEncoder passwordEncoder; // password는 데이터베이스에 인코딩되어 저장되어야한다. (시큐리티에서 막음)

    public EmployeeServiceImpl(EmployeeDAO employeeDAO, BCryptPasswordEncoder passwordEncoder){
        this.employeeDAO = employeeDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean validate(String username) {
        return employeeDAO.validate(username) == null;
    }

    @Override
    public EmployeeDTO selectOne(int id) {
        return employeeDAO.selectOne(id);
    }

    @Override
    public List<EmployeeDTO> selectAll() {
        return employeeDAO.selectAll();
    }

    @Override
    public void update(EmployeeDTO employeeDTO){
        employeeDAO.update(employeeDTO);
    }
    @Override
    public void delete(int id) {
        employeeDAO.delete(id);
    }

    @Override
    public EmployeeDTO auth(EmployeeDTO attempt) {
        attempt.setPassword(passwordEncoder.encode(attempt.getPassword()));
        System.out.println(attempt.getPassword());
        return employeeDAO.auth(attempt);
    }

    @Override
    public boolean register(EmployeeDTO attempt) {
        attempt.setPassword(passwordEncoder.encode(attempt.getPassword()));
        attempt.setRole("auth");
        if (validate(attempt.getUsername())) {
            employeeDAO.register(attempt);
            return true;
        } else {
            return false;
        }
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

