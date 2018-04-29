package com.test.perf.service.facade;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.perf.common.constant.Constants;
import com.test.perf.common.vo.Response;

@RestController
public class TestFacade extends BaseController {

	@RequestMapping(Constants.HELLOWORLD)
	public Response<String> helloworld(Long random) {
		return new Response<>("Hello world: "+random);
	}
	
}
