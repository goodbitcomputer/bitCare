package com.bit.bitcare.config;

import com.bit.bitcare.handler.UserAuthFailHandler;
import com.bit.bitcare.handler.UserAuthSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
                .tokenValiditySeconds(60000)
                .key("sampleKey")
                .authenticationSuccessHandler(loginSuccessHandler());

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
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

}