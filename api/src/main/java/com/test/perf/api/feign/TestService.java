package com.test.perf.api.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.test.perf.api.config.FeignConfig;
import com.test.perf.common.constant.Constants;
import com.test.perf.common.vo.Response;

@FeignClient(name = Constants.SERVICE_NAME, configuration = FeignConfig.class)
public interface TestService {

	@RequestMapping(value = Constants.HELLOWORLD, method = RequestMethod.POST)
	Response<String> helloworld(@RequestParam("sid") String sid, @RequestParam("random") Long random);

}
