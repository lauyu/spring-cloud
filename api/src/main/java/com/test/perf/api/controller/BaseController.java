package com.test.perf.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.test.perf.api.config.SysConfig;
import com.test.perf.common.constant.ErrorCode;
import com.test.perf.common.util.RandomUtil;
import com.test.perf.common.vo.Response;

public abstract class BaseController {

	private static final Logger log = LoggerFactory.getLogger(BaseController.class);

	@Autowired
	SysConfig config;
	
	@ExceptionHandler(Exception.class)
	public Response<String> name(Exception ex) {
		log.info("发生未知错误", ex);
		return new Response<>(ErrorCode.UNKONWN, null);
	}
	
	String getSid() {
		if(config.isFakeSid()) {
			return "1234567890";
		}
		return RandomUtil.getUUID();
	}
}
