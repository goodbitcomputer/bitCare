package com.bit.bitcare.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Date;

/**
 * ---------------------------------------------------------------------------
 * 2023.05.16
 *
 * PersistentTokenRepositoryImpl
 * PersistentTokenRepository 인터페이스에 대한 구현체
 * DataSource, JdbcTokenRepositoryImpl 객체 사용
 * 메소드 목록
 * 1. createNewToken : 새 토큰을 생성 및 저장하는 메소드
 * 2. updateToken : 토큰 정보를 업데이트 하는 메소드
 * 3. getTokenForSeries : series 값으로 그에 해당하는 토큰을 리턴하는 메소드
 * 4. removeUserTokens : 토큰을 삭제하는 메소드
 */

@Repository
@RequiredArgsConstructor
public class PersistentTokenRepositoryImpl implements PersistentTokenRepository {

    private final DataSource dataSource;
    private final JdbcTokenRepositoryImpl persistentTokenRepository;

    @Autowired
    public PersistentTokenRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        persistentTokenRepository = new JdbcTokenRepositoryImpl();
        persistentTokenRepository.setDataSource(dataSource);
    }

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        persistentTokenRepository.createNewToken(token);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        persistentTokenRepository.updateToken(series, tokenValue, lastUsed);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        return persistentTokenRepository.getTokenForSeries(seriesId);
    }

    @Override
    public void removeUserTokens(String username) {
        persistentTokenRepository.removeUserTokens(username);
    }
}