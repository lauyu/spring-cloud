package com.test.perf.api.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Request;

/** 
 * @ClassName: FeignConfig 
 * @Description: Feign Client的配置，
 * @author yu.liu
 * @date 2017年10月12日 上午10:32:47 
 *  
 */
@Configuration
@EnableFeignClients(basePackages="com.test.**.feign")
public class FeignConfig {

	@Bean
    Request.Options feignOptions() {
        return new Request.Options(/**connectTimeoutMillis**/60 * 1000, /** readTimeoutMillis **/60 * 1000);
    }
    
}
