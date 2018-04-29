package com.test.perf.api.controller;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.perf.api.feign.TestService;
import com.test.perf.common.vo.Response;

@RestController
@RequestMapping("test/")
public class ApiController extends BaseController {

	@Autowired
	TestService testService;
	
	@RequestMapping("helloworld")
	public Response<String> helloworld() {
		return testService.helloworld(getSid(), RandomUtils.nextLong());
	}
	
}
