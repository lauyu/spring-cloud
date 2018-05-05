package com.test.perf.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.ObservableExecutionMode;
import com.test.perf.api.feign.FeignTestService;
import com.test.perf.common.constant.ErrorCode;
import com.test.perf.common.vo.Response;

@Service
public class TestService {

	@Autowired
	private FeignTestService feignService;
	
	@HystrixCommand(fallbackMethod="helloCallback", defaultFallback="defaultCallback")
	public Response<String>  helloworld(String sid, Long random) {
//		feignService.noexist();;
		return feignService.helloworld(sid, random);
//		return response.getData();
//		return null;
	}
	
	@HystrixCommand(commandKey="testservice-delay", defaultFallback="defaultCallback")
	public void delay(String sid) {
		System.out.println("execute delay.............");
		feignService.delay(sid, 40);
//		feignService.noexist();
		System.out.println("end execute delay.............");
	}
	
	@HystrixCommand(fallbackMethod="asyncCallback", defaultFallback="defaultCallback", observableExecutionMode=ObservableExecutionMode.LAZY)
	public void testAsync(String sid) {
//		feignService.delay(sid, 10000);
		System.out.println("execute testAsync.............");
		feignService.noexist();
		System.out.println("end execute testAsync.............");
	}
	
	private Response<String> helloCallback(String sid, Long random) {
		String msg = "break from call: helloworld("+sid+","+random+")";
		return new Response<String>(ErrorCode.UNKONWN, msg);
	}
	
	private void defaultCallback() {
		System.out.println("default callback in hystric break");
	}
	
	private void asyncCallback(String sid) {
		System.out.println("asyncCallback in hystric break");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
