package com.test.perf.service.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.perf.common.log.ContextFixFilter;
import com.test.perf.common.log.LoggerFilter;
import com.test.perf.entity.City;

@Configuration
//@EnableEurekaClient
@EnableDiscoveryClient
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
	
//	@Bean
//    public RedisTemplate<String, City> redisTemplate(RedisConnectionFactory factory){
//		RedisTemplate<String, City> template = new RedisTemplate(factory);
//        setSerializer(template);//设置序列化工具
//        template.afterPropertiesSet();
//        return template;
//    }
//	
//	private void setSerializer(RedisTemplate<String, City> template){
//        @SuppressWarnings({ "rawtypes", "unchecked" })
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper om = new ObjectMapper();
////        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//	}
}
