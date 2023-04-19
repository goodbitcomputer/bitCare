package com.bit.bitcare.service;


import com.bit.bitcare.model.MemberDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    private final String NAMESPACE = "mapper.MemberMapper";
    private SqlSession session;
    private BCryptPasswordEncoder passwordEncoder; // password는 데이터베이스에 인코딩되어 저장되어야한다. (시큐리티에서 막음)

    @Autowired
    public MemberService(SqlSession session) {
        this.session = session;
    }


    public boolean validate(String username) {
        return session.selectOne(NAMESPACE + ".validate", username) == null;
    }


    public MemberDTO selectOne(int id) {
        return session.selectOne(NAMESPACE + ".selectOne", id);
    }
    public List<MemberDTO> selectAll() {
        return session.selectList(NAMESPACE + ".selectAll");
    }

    public void delete(int id) {
        session.delete(NAMESPACE + ".delete", id);
    }


}

