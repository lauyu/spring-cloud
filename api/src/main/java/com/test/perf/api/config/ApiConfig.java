package com.test.perf.api.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.test.perf.common.log.ContextFixFilter;
import com.test.perf.common.log.LoggerFilter;

@Configuration
@EnableDiscoveryClient
@EnableConfigurationProperties({SysConfig.class})
public class ApiConfig {
	
	@Bean
    public FilterRegistrationBean contextFixFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new ContextFixFilter());
        registration.addUrlPatterns("/*");
        //确保MsmartContext维护拦截器在loggerFilter之前，spring security的FilterChainProxy之前被执行
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE+1);
        return registration;
    }
	
	@Bean
    public FilterRegistrationBean loggerFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new LoggerFilter());
        registration.addUrlPatterns("/*");
        registration.setName("loggerFilter");
        //注意，参数打印的拦截器，需要在filter的最前列，但是要在ContextFixFilter之后
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE+2);
        return registration;
    }
}
