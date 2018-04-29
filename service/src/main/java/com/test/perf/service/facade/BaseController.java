package com.test.perf.service.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.test.perf.common.constant.ErrorCode;
import com.test.perf.common.vo.Response;

public abstract class BaseController {

	private static final Logger log = LoggerFactory.getLogger(BaseController.class);

	@ExceptionHandler(Exception.class)
	public Response<String> name(Exception ex) {
		log.info("发生未知错误", ex);
		return new Response(ErrorCode.UNKONWN, null);
	}
	
}
