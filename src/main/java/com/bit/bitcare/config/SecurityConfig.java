package com.bit.bitcare.config;

import com.bit.bitcare.handler.UserAuthFailHandler;
import com.bit.bitcare.handler.UserAuthSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Arrays;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * ---------------------------------------------------------------------------
     * 2023.05.15
     *
     * authenticationManager
     * AuthenticationManager Bean 객체 생성
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * ---------------------------------------------------------------------------
     * 2023.05.16
     *
     * userDetailService
     * UserDetailsService 객체 주입
     */
    @Autowired
    private UserDetailsService userDetailService;

    /*
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("");
    }
     */

    /**
     * ---------------------------------------------------------------------------
     * 2023.05.16
     *
     * dataSource
     * DataSource 객체 주입 - 객체는 application.properties 설정에 따라 생성됨
     */
    @Autowired
    private DataSource dataSource;

    /**
     * ---------------------------------------------------------------------------
     * 2023.05.15
     *
     * WebSecurityCustomizer
     * 정적 자원에 대해 security 적용 제외 (/static)
     * ---------------------------------------------------------------------------
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 정적 자원에 대해서 Security를 적용하지 않음으로 설정
        return (web) -> web.ignoring().antMatchers("/assets/**", "/css/**", "/img/**", "/js/**");
    }

    /**
     * ---------------------------------------------------------------------------
     * 2023.05.15
     *
     * securityFilterChain
     * security 설정을 정의하는 메소드
     * "/login","/auth","/user/register","/m.home"에 대해 permitAll 권한 부여 - 모든 접근 허용
     * 그 외 url에 대해선 authenticated 권한 부여 - 인증 후 접근 허용
     * rememberMe 쿠키 사용 설정
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests() //<security:intercept-url>
                .antMatchers("/", "/api/**","/autoLogin","/login","/auth","/user/register", "/mobile/home", "/mobile/login").permitAll() //permitAll 권한부여
                .anyRequest().authenticated() //나머지 url에 대해 authenticated 권한 부여
                .and().formLogin() //<security:form-login>
                .loginPage("/")
                .and().csrf().disable()
                .logout()
                .logoutUrl("/logOut")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .and().rememberMe()
                .alwaysRemember(true)
                .tokenValiditySeconds(60 * 60 * 24) // Remember Me 쿠키의 유효시간을 1일로 지정
                .key("uniqueAndSecret") // Remember Me 쿠키의 key 값
                .rememberMeParameter("rememberMe") // Remember Me 쿠키가 적용될 파라미터 이름
                .userDetailsService(userDetailsService())
                .authenticationSuccessHandler(loginSuccessHandler())
                .tokenRepository(persistentTokenRepository()) // Remember Me 쿠키를 저장하는 데이터베이스 저장소
                .and().cors()
                .and().httpBasic()
                .authenticationEntryPoint((request, response, authException) -> {
                    //>>> response.addHeader("WWW-Authenticate", "Basic realm=\"" + realmName + "\""); <<< (((REMOVED)))
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
                }).and().build();
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

    @Bean
    protected UserDetailsService userDetailsService() {
        return userDetailService;
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*")); // 허용할 도메인 주소 설정
        configuration.setAllowedMethods(Arrays.asList("GET","POST")); // 허용할 HTTP 메서드 설정
        configuration.setAllowCredentials(true); // 쿠키 사용 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}