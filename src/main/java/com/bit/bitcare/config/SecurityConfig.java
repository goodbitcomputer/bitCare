package com.bit.bitcare.config;

import com.bit.bitcare.handler.UserAuthFailHandler;
import com.bit.bitcare.handler.UserAuthSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailService;

    /*
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("");
    }
     */

    @Autowired
    private DataSource dataSource;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests() //<security:intercept-url>
                .antMatchers("/logIn","/user/register").permitAll() //permitAll 권한부여
                .anyRequest().authenticated(); //나머지 url에 대해 authenticated 권한 부여

        http.formLogin() //<security:form-login>
                .loginPage("/logIn")
                .loginProcessingUrl("/doLogin")
                .defaultSuccessUrl("/")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(loginSuccessHandler())
                .failureHandler(loginFailureHandler());

        http.csrf().disable();

        http.logout()
                .logoutUrl("/user/logOut")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true);

        http.rememberMe()
                .tokenValiditySeconds(60 * 60 * 24) // Remember Me 쿠키의 유효시간을 1일로 지정
                .key("uniqueAndSecret") // Remember Me 쿠키의 key 값
                .rememberMeParameter("rememberMe") // Remember Me 쿠키가 적용될 파라미터 이름
                .userDetailsService(userDetailsService())
                .authenticationSuccessHandler(loginSuccessHandler())
                .tokenRepository(persistentTokenRepository()); // Remember Me 쿠키를 저장하는 데이터베이스 저장소

        http.cors();

        http.httpBasic();
    }

    @Bean
    public UserAuthSuccessHandler loginSuccessHandler() {
        return new UserAuthSuccessHandler();
    }

    @Bean
    public UserAuthFailHandler loginFailureHandler() {
        return new UserAuthFailHandler();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailService;
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

}