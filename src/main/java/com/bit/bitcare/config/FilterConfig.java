package com.bit.bitcare.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.filter.OrderedCharacterEncodingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
public class FilterConfig {
    /**
     * ---------------------------------------------------------------------------
     * 2023.05.16
     *
     * encodeFilterBean
     * MS949 인코딩 필터 적용
     */
    @Bean
    public FilterRegistrationBean  encodeFilterBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        CharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
        filter.setForceEncoding(true);
        filter.setEncoding("MS949");
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/ms949filterUrl/*");
        return registrationBean;
    }
}
