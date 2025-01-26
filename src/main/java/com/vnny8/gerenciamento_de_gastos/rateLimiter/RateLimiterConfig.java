package com.vnny8.gerenciamento_de_gastos.rateLimiter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {

    @Bean
    public RateLimiterFilter rateLimiterFilter() {
        return new RateLimiterFilter();
    }

    @Bean
    public FilterRegistrationBean<RateLimiterFilter> rateLimiterFilterRegistration(RateLimiterFilter rateLimiterFilter) {
        FilterRegistrationBean<RateLimiterFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(rateLimiterFilter);
        registrationBean.addUrlPatterns("/api/*"); // Aplica o filtro apenas às URLs da sua API
        registrationBean.setOrder(1); // Define a ordem do filtro
        return registrationBean;
    }
}
