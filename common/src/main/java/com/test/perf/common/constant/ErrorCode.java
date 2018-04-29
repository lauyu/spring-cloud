package com.test.perf.common.constant;

public enum ErrorCode {

	SUCCESS(0, "success"),
	
	UNKONWN(9999, "unknown error");
	
	private int code;
	
	private String msg;
	
	private ErrorCode(int code, String msg) {
		this.code=code;
		this.msg=msg;
	}
	
	
	
	public int getCode() {
		return code;
	}


	public String getMsg() {
		return msg;
	}



	public String toString() {
		return "ErrorCode[code="+code+", msg="+msg+"]";
	}
	
}
