package com.bit.bitcare.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Date;

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