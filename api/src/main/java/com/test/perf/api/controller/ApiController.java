package com.test.perf.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test/perf")
public class ApiController {

	public String helloworld() {
		return "hello world";
	}
	
}
