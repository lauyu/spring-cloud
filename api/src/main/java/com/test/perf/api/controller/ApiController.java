package com.test.perf.api.controller;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.perf.api.config.SysConfig;
import com.test.perf.api.feign.TestService;
import com.test.perf.common.vo.Response;
import com.test.perf.entity.City;

@RestController
@RequestMapping("test/")
public class ApiController extends BaseController {

	@Autowired
	TestService testService;
	
	@Autowired
	SysConfig sysConfig;
	
	@RequestMapping("helloworld")
	public Response<String> helloworld() {
		return testService.helloworld(getSid(), RandomUtils.nextLong());
	}
	
	@RequestMapping("cache/read")
	public Response<City> cacheRead(Integer id) {
		return testService.redisRead(getSid(), id);
	}
	
	@RequestMapping("cache/write")
	public Response<?> cacheWrite(City city) {
		return testService.redisWrite(getSid(), city);
	}
	
	
}
