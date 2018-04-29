package com.test.perf.common.vo;

import com.test.perf.common.constant.ErrorCode;
import com.test.perf.common.util.JsonUtil;

public class Response<T> {

	int code;
	
	String msg;
	
	T data;
	
	public Response() {
		this(ErrorCode.SUCCESS, null);
	}
	
	public Response(T data) {
		this(ErrorCode.SUCCESS, data);
	}
	
	public Response(ErrorCode errorCode) {
		this(errorCode.getCode(), errorCode.getMsg(), null);
	}
	
	public Response(ErrorCode errorCode, T data) {
		this(errorCode.getCode(), errorCode.getMsg(), data);
	}

	public Response(int code, String msg, T data) {
		this.code=code;
		this.msg=msg;
		this.data=data;
	}
	
	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public T getData() {
		return data;
	}
	
	public String toString() {
		return "Response[code="+code+", msg="+msg+", data="+JsonUtil.toJson(data)+"]";
	}
	
}
